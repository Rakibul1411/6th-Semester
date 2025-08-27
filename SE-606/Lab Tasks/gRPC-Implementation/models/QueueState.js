const mongoose = require('mongoose');

const queueStateSchema = new mongoose.Schema({
  operationId: {
    type: String,
    required: true,
    unique: true
  },
  operationType: {
    type: String,
    required: true,
    enum: ['CREATE', 'UPDATE', 'DELETE', 'GET', 'LIST']
  },
  userId: {
    type: String,
    required: false
  },
  data: {
    type: String, // JSON stringified data
    required: false
  },
  status: {
    type: String,
    enum: ['PENDING', 'PROCESSING', 'COMPLETED', 'FAILED', 'RETRY'],
    default: 'PENDING'
  },
  errorMessage: {
    type: String,
    required: false
  },
  retryCount: {
    type: Number,
    default: 0
  },
  maxRetries: {
    type: Number,
    default: 3
  }
}, {
  timestamps: true
});

// Index for better query performance
queueStateSchema.index({ status: 1, createdAt: 1 });
queueStateSchema.index({ operationId: 1 });

module.exports = mongoose.model('QueueState', queueStateSchema);
