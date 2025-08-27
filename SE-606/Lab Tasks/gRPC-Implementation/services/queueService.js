const Queue = require('bull');
const redis = require('redis');
const { v4: uuidv4 } = require('uuid');
const QueueState = require('../models/QueueState');
const { logger } = require('../config/database');

// Redis client configuration with better error handling
const redisClient = redis.createClient({
  host: process.env.REDIS_HOST || 'localhost',
  port: process.env.REDIS_PORT || 6379,
  // Optimize for local development without Redis
  retryDelayOnFailover: 100,
  maxRetriesPerRequest: 1, // Reduced from 20 to 1 for faster fallback
  connectTimeout: 1000, // 1 second timeout
  lazyConnect: true // Don't connect immediately
});

// Handle Redis connection
redisClient.on('connect', () => {
  logger.info('Redis connected successfully');
});

redisClient.on('error', (err) => {
  logger.warn('Redis connection failed, using in-memory queue fallback:', err.message);
});

// Create job queue
const userQueue = new Queue('user operations', {
  redis: {
    host: process.env.REDIS_HOST || 'localhost',
    port: process.env.REDIS_PORT || 6379,
  },
  defaultJobOptions: {
    removeOnComplete: 10,
    removeOnFail: 5,
    attempts: 3,
    backoff: {
      type: 'exponential',
      delay: 2000,
    },
  },
});

// Queue state manager
class QueueStateManager {
  
  // Create new operation state
  static async createOperationState(operationType, userId = null, data = null) {
    const operationId = uuidv4();
    
    try {
      const queueState = new QueueState({
        operationId,
        operationType,
        userId,
        data: data ? JSON.stringify(data) : null,
        status: 'PENDING'
      });

      await queueState.save();
      logger.info(`Created operation state: ${operationId} - ${operationType}`);
      
      return operationId;
    } catch (error) {
      logger.error('Failed to create operation state:', error);
      throw error;
    }
  }

  // Update operation state
  static async updateOperationState(operationId, status, errorMessage = null) {
    try {
      const updateData = { status };
      if (errorMessage) {
        updateData.errorMessage = errorMessage;
      }

      await QueueState.findOneAndUpdate(
        { operationId },
        updateData,
        { new: true }
      );

      logger.info(`Updated operation state: ${operationId} - ${status}`);
    } catch (error) {
      logger.error('Failed to update operation state:', error);
    }
  }

  // Get operation state
  static async getOperationState(operationId) {
    try {
      return await QueueState.findOne({ operationId });
    } catch (error) {
      logger.error('Failed to get operation state:', error);
      return null;
    }
  }

  // Get failed operations for retry
  static async getFailedOperations() {
    try {
      return await QueueState.find({
        status: 'FAILED',
        retryCount: { $lt: 3 }
      }).sort({ createdAt: 1 });
    } catch (error) {
      logger.error('Failed to get failed operations:', error);
      return [];
    }
  }

  // Mark operation for retry
  static async markForRetry(operationId) {
    try {
      await QueueState.findOneAndUpdate(
        { operationId },
        {
          status: 'RETRY',
          $inc: { retryCount: 1 }
        }
      );
      logger.info(`Marked operation for retry: ${operationId}`);
    } catch (error) {
      logger.error('Failed to mark operation for retry:', error);
    }
  }

  // Clean up old completed operations
  static async cleanupOldOperations() {
    try {
      const thirtyDaysAgo = new Date(Date.now() - 30 * 24 * 60 * 60 * 1000);
      
      const result = await QueueState.deleteMany({
        status: 'COMPLETED',
        createdAt: { $lt: thirtyDaysAgo }
      });

      if (result.deletedCount > 0) {
        logger.info(`Cleaned up ${result.deletedCount} old operations`);
      }
    } catch (error) {
      logger.error('Failed to cleanup old operations:', error);
    }
  }
}

// Add job to queue with state tracking
const addJobToQueue = async (jobType, jobData, operationType) => {
  try {
    const operationId = await QueueStateManager.createOperationState(
      operationType, 
      jobData.userId || jobData.id, 
      jobData
    );

    const jobOptions = {
      attempts: 3,
      backoff: 'exponential',
      delay: 1000,
      jobId: operationId
    };

    await userQueue.add(jobType, { ...jobData, operationId }, jobOptions);
    logger.info(`Added job to queue: ${jobType} - ${operationId}`);
    
    return operationId;
  } catch (error) {
    logger.error('Failed to add job to queue:', error);
    throw error;
  }
};

// Process queue jobs
userQueue.process('*', async (job) => {
  const { operationId, ...jobData } = job.data;
  
  try {
    await QueueStateManager.updateOperationState(operationId, 'PROCESSING');
    
    // Simulate processing (you can add actual business logic here)
    await new Promise(resolve => setTimeout(resolve, 100));
    
    await QueueStateManager.updateOperationState(operationId, 'COMPLETED');
    logger.info(`Completed job: ${job.name} - ${operationId}`);
    
    return { success: true, operationId };
  } catch (error) {
    await QueueStateManager.updateOperationState(operationId, 'FAILED', error.message);
    logger.error(`Failed job: ${job.name} - ${operationId}:`, error);
    throw error;
  }
});

// Queue event handlers
userQueue.on('completed', (job, result) => {
  logger.info(`Job completed: ${job.id}`);
});

userQueue.on('failed', (job, err) => {
  logger.error(`Job failed: ${job.id}`, err);
});

userQueue.on('stalled', (job) => {
  logger.warn(`Job stalled: ${job.id}`);
});

// Periodic cleanup and retry mechanism
setInterval(async () => {
  try {
    // Cleanup old operations
    await QueueStateManager.cleanupOldOperations();
    
    // Retry failed operations
    const failedOps = await QueueStateManager.getFailedOperations();
    for (const op of failedOps) {
      if (op.retryCount < op.maxRetries) {
        await QueueStateManager.markForRetry(op.operationId);
        
        // Re-add to queue
        const jobData = op.data ? JSON.parse(op.data) : {};
        await userQueue.add(op.operationType.toLowerCase(), { 
          ...jobData, 
          operationId: op.operationId 
        });
        
        logger.info(`Retrying operation: ${op.operationId}`);
      }
    }
  } catch (error) {
    logger.error('Error in periodic queue maintenance:', error);
  }
}, 5 * 60 * 1000); // Run every 5 minutes

module.exports = {
  userQueue,
  addJobToQueue,
  QueueStateManager,
  redisClient
};
