const express = require('express');
const cors = require('cors');
const grpc = require('@grpc/grpc-js');
const protoLoader = require('@grpc/proto-loader');
const path = require('path');
const { logger } = require('./config/database');

// Create Express app for REST API Gateway
const app = express();

// Middleware
app.use(cors());
app.use(express.json());
app.use(express.static('public'));

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

// Create gRPC client
const grpcClient = new userProto.UserService(
  'localhost:50051',
  grpc.credentials.createInsecure()
);

// Utility function to handle gRPC calls with promises
function promisifyGrpcCall(method, request) {
  return new Promise((resolve, reject) => {
    method(request, (error, response) => {
      if (error) {
        reject(error);
      } else {
        resolve(response);
      }
    });
  });
}

// REST API Routes

// GET /api/users - List users with pagination and search
app.get('/api/users', async (req, res) => {
  try {
    const { page = 1, limit = 10, search = '' } = req.query;
    
    const response = await promisifyGrpcCall(grpcClient.listUsers.bind(grpcClient), {
      page: parseInt(page),
      limit: parseInt(limit),
      search: search.toString()
    });

    res.json(response);
  } catch (error) {
    logger.error('Error listing users:', error);
    res.status(500).json({
      success: false,
      message: 'Failed to list users: ' + error.message
    });
  }
});

// GET /api/users/:id - Get specific user
app.get('/api/users/:id', async (req, res) => {
  try {
    const { id } = req.params;
    
    const response = await promisifyGrpcCall(grpcClient.getUser.bind(grpcClient), {
      id
    });

    if (response.success) {
      res.json(response);
    } else {
      res.status(404).json(response);
    }
  } catch (error) {
    logger.error('Error getting user:', error);
    res.status(500).json({
      success: false,
      message: 'Failed to get user: ' + error.message
    });
  }
});

// POST /api/users - Create new user
app.post('/api/users', async (req, res) => {
  try {
    const { name, email, password, phone, role } = req.body;
    
    const response = await promisifyGrpcCall(grpcClient.createUser.bind(grpcClient), {
      name,
      email,
      password,
      phone,
      role
    });

    if (response.success) {
      res.status(201).json(response);
    } else {
      res.status(400).json(response);
    }
  } catch (error) {
    logger.error('Error creating user:', error);
    res.status(500).json({
      success: false,
      message: 'Failed to create user: ' + error.message
    });
  }
});

// PUT /api/users/:id - Update user
app.put('/api/users/:id', async (req, res) => {
  try {
    const { id } = req.params;
    const { name, email, phone, role } = req.body;
    
    const response = await promisifyGrpcCall(grpcClient.updateUser.bind(grpcClient), {
      id,
      name,
      email,
      phone,
      role
    });

    if (response.success) {
      res.json(response);
    } else {
      res.status(400).json(response);
    }
  } catch (error) {
    logger.error('Error updating user:', error);
    res.status(500).json({
      success: false,
      message: 'Failed to update user: ' + error.message
    });
  }
});

// DELETE /api/users/:id - Delete user
app.delete('/api/users/:id', async (req, res) => {
  try {
    const { id } = req.params;
    
    const response = await promisifyGrpcCall(grpcClient.deleteUser.bind(grpcClient), {
      id
    });

    if (response.success) {
      res.json(response);
    } else {
      res.status(404).json(response);
    }
  } catch (error) {
    logger.error('Error deleting user:', error);
    res.status(500).json({
      success: false,
      message: 'Failed to delete user: ' + error.message
    });
  }
});

// Health check endpoint
app.get('/api/health', (req, res) => {
  res.json({
    status: 'OK',
    timestamp: new Date().toISOString(),
    service: 'REST API Gateway for gRPC User Management'
  });
});

// Error handling middleware
app.use((error, req, res, next) => {
  logger.error('Unhandled error:', error);
  res.status(500).json({
    success: false,
    message: 'Internal server error'
  });
});

// 404 handler
app.use((req, res) => {
  res.status(404).json({
    success: false,
    message: 'Route not found'
  });
});

// Start server
const PORT = process.env.REST_PORT || 4000;

app.listen(PORT, () => {
  logger.info(`REST API Gateway running on port ${PORT}`);
  logger.info(`Frontend available at: http://localhost:${PORT}`);
  logger.info(`API endpoints available at: http://localhost:${PORT}/api`);
});

module.exports = app;
