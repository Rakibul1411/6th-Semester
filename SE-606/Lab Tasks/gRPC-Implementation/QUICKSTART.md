# Quick Start Guide - gRPC User Management System

## 🚀 Fast Manual Setup (Recommended)

### Step 1: Install Dependencies (only once)
```bash
npm install
```

### Step 2: Start gRPC Server (Terminal 1)
```bash
npm start
```
**Result:** gRPC server runs on `localhost:50051`

### Step 3: Start REST API Gateway (Terminal 2)
```bash
npm run gateway
```
**Result:** Frontend + REST API runs on `http://localhost:4000`

### Step 4: Open Your Browser
Go to: **http://localhost:4000**

---

## 🔧 Alternative: Using the Automated Script
```bash
chmod +x start.sh
./start.sh
```
*(This starts both services automatically but takes longer)*

---

## 🧪 Testing the System

### 1. Web Interface Testing
- Open `http://localhost:4000` in your browser
- Use the dashboard to view system status
- Click "Add User" to create new users
- Search, edit, and delete users from the user list

### 2. REST API Testing (using curl)
```bash
# Create a user
curl -X POST http://localhost:4000/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","password":"123456","phone":"+1234567890","role":"user"}'

# List users
curl http://localhost:4000/api/users

# Get specific user (replace USER_ID)
curl http://localhost:4000/api/users/USER_ID

# Update user (replace USER_ID)
curl -X PUT http://localhost:4000/api/users/USER_ID \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated Name","phone":"+9876543210"}'

# Delete user (replace USER_ID)
curl -X DELETE http://localhost:4000/api/users/USER_ID
```

### 3. gRPC Client Testing
```bash
# Test all gRPC operations
npm run client

# Individual operations
node client.js create    # Create user
node client.js list      # List users
node client.js search john  # Search users
```

### 4. Health Checks
```bash
# REST API health
curl http://localhost:4000/api/health

# gRPC health (via HTTP monitoring)
curl http://localhost:3000/health

# Queue status
curl http://localhost:3000/queue/status
```

---

## 📊 System Architecture

```
Browser (localhost:4000)
       ↓
REST API Gateway (Port 4000)
       ↓
gRPC Server (Port 50051)
       ↓
MongoDB (Atlas Cloud)
       ↓
Message Queue (Redis/In-memory)
```

---

## 🛠️ Troubleshooting

### Port Already in Use
```bash
# Kill processes on specific ports
lsof -ti:4000 | xargs kill -9  # REST Gateway
lsof -ti:50051 | xargs kill -9 # gRPC Server
lsof -ti:3000 | xargs kill -9  # Health Monitor
```

### Can't Connect to MongoDB
- Check your internet connection
- MongoDB Atlas connection string is embedded in the code
- No additional setup needed for the database

### Redis Connection Failed
- This is optional - the system uses in-memory fallback
- Install Redis if you want persistent queue: `brew install redis` (macOS)

---

## 🎯 Quick Demo Flow

1. **Start servers** (2 terminals or use script)
2. **Open browser** → `http://localhost:4000`
3. **Create 2-3 users** using the web interface
4. **Test search functionality**
5. **Edit a user's information**
6. **Delete a user**
7. **Check queue status** → `http://localhost:3000/queue/status`

---

## 📱 Frontend Features

- **Dashboard:** System overview with statistics
- **User Management:** Full CRUD operations
- **Search & Filter:** Real-time search with role filtering
- **Pagination:** Handle large user lists
- **Responsive Design:** Works on mobile and desktop
- **Real-time Alerts:** Success/error notifications
- **Queue Monitoring:** Track background operations

---

## ⚡ Performance Tips

- Use pagination for large datasets
- Search is debounced (500ms) to reduce API calls
- All operations are tracked in the message queue
- Automatic retry mechanism for failed operations
