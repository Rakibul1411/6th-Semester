const grpc = require('@grpc/grpc-js');
const protoLoader = require('@grpc/proto-loader');
const path = require('path');
const express = require('express');
const { connectDB, logger } = require('./config/database');
const UserService = require('./services/userService');

// Create Express app for health checks and monitoring
const app = express();
app.use(express.json());

// Add CORS headers for cross-origin requests
app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');
  res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
  next();
});

// Health check endpoint
app.get('/health', (req, res) => {
  res.json({
    status: 'OK',
    timestamp: new Date().toISOString(),
    service: 'gRPC User Management Service'
  });
});

// Queue status endpoint
app.get('/queue/status', async (req, res) => {
  try {
    const { QueueStateManager } = require('./services/queueService');
    
    // Get queue statistics with error handling
    const [pending, processing, completed, failed] = await Promise.all([
      require('./models/QueueState').countDocuments({ status: 'PENDING' }).catch(() => 0),
      require('./models/QueueState').countDocuments({ status: 'PROCESSING' }).catch(() => 0),
      require('./models/QueueState').countDocuments({ status: 'COMPLETED' }).catch(() => 0),
      require('./models/QueueState').countDocuments({ status: 'FAILED' }).catch(() => 0)
    ]);

    res.json({
      status: 'OK',
      queue_status: {
        pending,
        processing,
        completed,
        failed,
        total: pending + processing + completed + failed,
        redis_connected: process.env.REDIS_AVAILABLE === 'true' || false
      },
      timestamp: new Date().toISOString(),
      message: pending + processing + completed + failed > 0 ? 
        `${pending + processing + completed + failed} operations tracked` : 
        'Queue operational (Redis fallback mode)'
    });
  } catch (error) {
    logger.error('Error getting queue status:', error);
    // Return fallback status instead of error
    res.json({
      status: 'OK',
      queue_status: {
        pending: 0,
        processing: 0,
        completed: 0,
        failed: 0,
        total: 0,
        redis_connected: false
      },
      timestamp: new Date().toISOString(),
      message: 'Queue running in fallback mode (Redis unavailable)'
    });
  }
});

// Queue operations endpoint - to see actual queue data
app.get('/queue/operations', async (req, res) => {
  try {
    const { limit = 50 } = req.query;
    const QueueState = require('./models/QueueState');
    
    const operations = await QueueState.find({})
      .sort({ createdAt: -1 })
      .limit(parseInt(limit))
      .lean();

    res.json({
      status: 'OK',
      operations: operations.map(op => ({
        id: op._id,
        operationId: op.operationId,
        operationType: op.operationType,
        status: op.status,
        userId: op.userId,
        retryCount: op.retryCount,
        errorMessage: op.errorMessage,
        createdAt: op.createdAt,
        updatedAt: op.updatedAt
      })),
      total: operations.length,
      timestamp: new Date().toISOString()
    });
  } catch (error) {
    logger.error('Error getting queue operations:', error);
    res.json({
      status: 'OK',
      operations: [],
      total: 0,
      timestamp: new Date().toISOString(),
      message: 'Could not load operations'
    });
  }
});

// Queue operations endpoint
app.get('/queue/operations', async (req, res) => {
  try {
    const limit = parseInt(req.query.limit) || 10;
    const QueueState = require('./models/QueueState');
    
    const operations = await QueueState.find({})
      .sort({ createdAt: -1 })
      .limit(limit)
      .lean();

    res.json({
      status: 'OK',
      operations: operations,
      count: operations.length,
      timestamp: new Date().toISOString()
    });
  } catch (error) {
    logger.error('Error getting queue operations:', error);
    res.json({
      status: 'OK',
      operations: [],
      count: 0,
      timestamp: new Date().toISOString(),
      message: 'No operations found (Redis fallback mode)'
    });
  }
});

// Load proto file
const PROTO_PATH = path.join(__dirname, 'proto', 'user.proto');
const packageDefinition = protoLoader.loadSync(PROTO_PATH, {
  keepCase: true,
  longs: String,
  enums: String,
  defaults: true,
  oneofs: true,
});

const userProto = grpc.loadPackageDefinition(packageDefinition).user;

// Create gRPC server
function startGrpcServer() {
  const server = new grpc.Server();

  // Add service implementation
  server.addService(userProto.UserService.service, {
    CreateUser: UserService.createUser,
    GetUser: UserService.getUser,
    UpdateUser: UserService.updateUser,
    DeleteUser: UserService.deleteUser,
    ListUsers: UserService.listUsers,
  });

  const PORT = process.env.GRPC_PORT || 50051;
  const HOST = process.env.GRPC_HOST || '0.0.0.0';

  server.bindAsync(
    `${HOST}:${PORT}`,
    grpc.ServerCredentials.createInsecure(),
    (error, port) => {
      if (error) {
        logger.error('Failed to start gRPC server:', error);
        process.exit(1);
      }

      server.start();
      logger.info(`gRPC server running on ${HOST}:${port}`);
    }
  );

  return server;
}

// Start HTTP server for health checks
function startHttpServer() {
  const HTTP_PORT = process.env.HTTP_PORT || 3000;
  
  app.listen(HTTP_PORT, () => {
    logger.info(`HTTP server running on port ${HTTP_PORT}`);
    logger.info(`Health check: http://localhost:${HTTP_PORT}/health`);
    logger.info(`Queue status: http://localhost:${HTTP_PORT}/queue/status`);
  });
}

// Initialize application
async function initializeApp() {
  try {
    // Connect to database
    await connectDB();
    
    // Start servers
    const grpcServer = startGrpcServer();
    startHttpServer();

    // Graceful shutdown
    const gracefulShutdown = (signal) => {
      logger.info(`Received ${signal}. Graceful shutdown...`);
      
      grpcServer.tryShutdown((error) => {
        if (error) {
          logger.error('Error during gRPC server shutdown:', error);
          grpcServer.forceShutdown();
        } else {
          logger.info('gRPC server shut down gracefully');
        }
      });

      // Close queue connections
      try {
        const { userQueue, redisClient } = require('./services/queueService');
        userQueue.close();
        redisClient.quit();
        logger.info('Queue connections closed');
      } catch (error) {
        logger.warn('Error closing queue connections:', error);
      }

      setTimeout(() => {
        process.exit(0);
      }, 5000);
    };

    process.on('SIGTERM', () => gracefulShutdown('SIGTERM'));
    process.on('SIGINT', () => gracefulShutdown('SIGINT'));

    logger.info('Application initialized successfully');

  } catch (error) {
    logger.error('Failed to initialize application:', error);
    process.exit(1);
  }
}

// Start the application
if (require.main === module) {
  initializeApp();
}

module.exports = { app, startGrpcServer, startHttpServer };
