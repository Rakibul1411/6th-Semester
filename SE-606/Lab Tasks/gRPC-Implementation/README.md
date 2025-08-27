# gRPC User Management System

A comprehensive user management system built with gRPC, Node.js, Express, MongoDB, and Redis message queue for state management and failure handling.

## Features

- **gRPC-based API** instead of RESTful endpoints
- **Complete CRUD operations** for user management
- **Message Queue integration** with Redis/Bull for state management
- **Failure handling and retry mechanism** for robust operations
- **MongoDB integration** with the provided connection string
- **Logging and monitoring** with Winston
- **Health checks and queue status** endpoints
- **Comprehensive error handling**

## Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   gRPC Client   │────│   gRPC Server   │────│    MongoDB      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              │
                       ┌─────────────────┐    ┌─────────────────┐
                       │  Message Queue  │────│   Queue State   │
                       │   (Redis/Bull)  │    │   Management    │
                       └─────────────────┘    └─────────────────┘
```

## Prerequisites

- Node.js (v14 or higher)
- MongoDB Atlas account (connection string provided)
- Redis (optional - will use in-memory fallback if not available)

## Installation

1. **Clone and install dependencies:**
   ```bash
   npm install
   ```

2. **Start the server:**
   ```bash
   npm start
   ```

3. **For development with auto-reload:**
   ```bash
   npm run dev
   ```

## Usage

### Starting the Server

```bash
npm start
```

The server will start:
- gRPC server on port `50051`
- HTTP server on port `3000` for health checks

### Using the Client

1. **Run comprehensive tests:**
   ```bash
   npm run client
   ```

2. **Individual operations:**
   ```bash
   # Create a user
   node client.js create

   # List all users
   node client.js list

   # Search users
   node client.js search "john"

   # Get specific user
   node client.js get <userId>

   # Delete user
   node client.js delete <userId>
   ```

### Health Monitoring

- **Health check:** `http://localhost:3000/health`
- **Queue status:** `http://localhost:3000/queue/status`

## gRPC API Reference

### Service Definition

```protobuf
service UserService {
  rpc CreateUser(CreateUserRequest) returns (UserResponse);
  rpc GetUser(GetUserRequest) returns (UserResponse);
  rpc UpdateUser(UpdateUserRequest) returns (UserResponse);
  rpc DeleteUser(DeleteUserRequest) returns (DeleteResponse);
  rpc ListUsers(ListUsersRequest) returns (ListUsersResponse);
}
```

### Message Types

#### CreateUserRequest
```protobuf
message CreateUserRequest {
  string name = 1;        // Required
  string email = 2;       // Required, unique
  string password = 3;    // Required
  string phone = 4;       // Required
  string role = 5;        // Optional: "admin", "user", "moderator"
}
```

#### User Response
```protobuf
message User {
  string id = 1;
  string name = 2;
  string email = 3;
  string phone = 4;
  string role = 5;
  string created_at = 6;
  string updated_at = 7;
}
```

## Message Queue System

The system implements a robust message queue for:

### State Management
- Tracks all operations with unique operation IDs
- Stores operation metadata in MongoDB
- Provides operation status tracking

### Failure Handling
- Automatic retry mechanism for failed operations
- Exponential backoff strategy
- Maximum retry limits
- Dead letter queue for permanently failed operations

### Queue States
- `PENDING`: Operation queued but not started
- `PROCESSING`: Operation currently being processed
- `COMPLETED`: Operation finished successfully
- `FAILED`: Operation failed (will be retried)
- `RETRY`: Operation marked for retry

## Database Schema

### User Model
```javascript
{
  name: String,        // Required
  email: String,       // Required, unique, lowercase
  password: String,    // Required
  phone: String,       // Required
  role: String,        // Enum: ['admin', 'user', 'moderator']
  createdAt: Date,     // Auto-generated
  updatedAt: Date      // Auto-generated
}
```

### Queue State Model
```javascript
{
  operationId: String,     // Unique operation identifier
  operationType: String,   // CREATE, UPDATE, DELETE, GET, LIST
  userId: String,          // Related user ID
  data: String,           // JSON stringified operation data
  status: String,         // PENDING, PROCESSING, COMPLETED, FAILED, RETRY
  errorMessage: String,   // Error details if failed
  retryCount: Number,     // Current retry attempt
  maxRetries: Number,     // Maximum retry attempts (default: 3)
  createdAt: Date,        // Timestamp
  updatedAt: Date         // Timestamp
}
```

## Configuration

### Environment Variables

```bash
# gRPC Server
GRPC_HOST=0.0.0.0
GRPC_PORT=50051

# HTTP Server (for health checks)
HTTP_PORT=3000

# Redis (optional)
REDIS_HOST=localhost
REDIS_PORT=6379

# MongoDB Configuration
# Create a .env file with your actual MongoDB Atlas connection string
# MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/database?retryWrites=true&w=majority
```

## Error Handling

The system provides comprehensive error handling:

1. **Validation Errors**: Missing required fields, invalid data
2. **Database Errors**: Connection issues, constraint violations
3. **Queue Errors**: Redis connection failures, job processing errors
4. **gRPC Errors**: Service unavailable, timeout errors

## Logging

Logs are written to:
- `logs/error.log`: Error-level logs only
- `logs/combined.log`: All logs
- Console: Real-time log output

## Testing

### Manual Testing with Client

```bash
# Run all tests
npm run client

# Test specific functionality
node client.js create
node client.js list
node client.js search "searchTerm"
```

### Example Test Flow

1. **Create Users**: Add sample users to the system
2. **Read Operations**: Get individual users and list all users
3. **Update Operations**: Modify user information
4. **Search**: Test search functionality
5. **Delete Operations**: Remove users from the system
6. **Error Testing**: Test duplicate emails, invalid data

## Production Considerations

1. **Security**: Implement authentication and authorization
2. **SSL/TLS**: Use secure connections for gRPC
3. **Rate Limiting**: Implement request rate limiting
4. **Monitoring**: Add comprehensive monitoring and alerting
5. **Scaling**: Consider horizontal scaling with load balancers
6. **Backup**: Implement database backup strategies

## Troubleshooting

### Common Issues

1. **MongoDB Connection**: Ensure the connection string is correct and network allows connections
2. **Redis Connection**: Redis is optional; the system will work without it using in-memory fallback
3. **Port Conflicts**: Change ports in environment variables if conflicts occur
4. **gRPC Client Connection**: Ensure server is running before starting client

### Logs Location
Check `logs/` directory for detailed error information and debugging.

## License

MIT License - feel free to modify and use for your projects.
