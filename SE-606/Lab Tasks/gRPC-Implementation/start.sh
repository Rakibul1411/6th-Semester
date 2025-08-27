#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🚀 Starting gRPC User Management System${NC}"
echo "=========================================="

# Function to check if a port is in use
check_port() {
    if lsof -Pi :$1 -sTCP:LISTEN -t >/dev/null ; then
        return 0
    else
        return 1
    fi
}

# Function to start a service in background
start_service() {
    local name=$1
    local command=$2
    local port=$3
    
    echo -e "${YELLOW}Starting $name...${NC}"
    
    if check_port $port; then
        echo -e "${RED}⚠️  Port $port is already in use. Please stop the existing service.${NC}"
        return 1
    fi
    
    eval "$command" &
    local pid=$!
    echo $pid > ".${name}.pid"
    
    # Wait a moment and check if process is still running
    sleep 2
    if kill -0 $pid 2>/dev/null; then
        echo -e "${GREEN}✅ $name started successfully (PID: $pid, Port: $port)${NC}"
        return 0
    else
        echo -e "${RED}❌ Failed to start $name${NC}"
        return 1
    fi
}

# Function to stop services
stop_services() {
    echo -e "\n${YELLOW}Stopping services...${NC}"
    
    for pidfile in .*.pid; do
        if [ -f "$pidfile" ]; then
            pid=$(cat "$pidfile")
            service_name=$(basename "$pidfile" .pid | sed 's/^.//')
            
            if kill -0 $pid 2>/dev/null; then
                echo -e "${YELLOW}Stopping $service_name (PID: $pid)...${NC}"
                kill $pid
                wait $pid 2>/dev/null
                echo -e "${GREEN}✅ $service_name stopped${NC}"
            fi
            
            rm "$pidfile"
        fi
    done
}

# Trap to stop services on script exit
trap stop_services EXIT INT TERM

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo -e "${RED}❌ Node.js is not installed. Please install Node.js first.${NC}"
    exit 1
fi

# Check if npm dependencies are installed
if [ ! -d "node_modules" ]; then
    echo -e "${YELLOW}📦 Installing dependencies...${NC}"
    npm install
    if [ $? -ne 0 ]; then
        echo -e "${RED}❌ Failed to install dependencies${NC}"
        exit 1
    fi
    echo -e "${GREEN}✅ Dependencies installed successfully${NC}"
fi

# Start gRPC Server
if ! start_service "grpc-server" "npm start" 50051; then
    echo -e "${RED}❌ Failed to start gRPC server${NC}"
    exit 1
fi

# Wait briefly for gRPC server to be ready
echo -e "${YELLOW}⏳ Waiting for gRPC server to be ready...${NC}"
sleep 2

# Start REST API Gateway
if ! start_service "rest-gateway" "npm run gateway" 4000; then
    echo -e "${RED}❌ Failed to start REST API Gateway${NC}"
    exit 1
fi

# Wait briefly for gateway to be ready
echo -e "${YELLOW}⏳ Waiting for REST API Gateway to be ready...${NC}"
sleep 1

echo -e "\n${GREEN}🎉 All services started successfully!${NC}"
echo "=========================================="
echo -e "${BLUE}📱 Frontend (Web UI):${NC}      http://localhost:4000"
echo -e "${BLUE}🔌 REST API Gateway:${NC}       http://localhost:4000/api"
echo -e "${BLUE}🏥 Health Check:${NC}           http://localhost:3000/health"
echo -e "${BLUE}📊 Queue Status:${NC}           http://localhost:3000/queue/status"
echo -e "${BLUE}🔧 gRPC Server:${NC}            localhost:50051"
echo "=========================================="
echo -e "${YELLOW}💡 Open your browser and go to http://localhost:4000 to use the web interface${NC}"
echo -e "${YELLOW}📖 Check the README.md for more information and API documentation${NC}"
echo ""
echo -e "${GREEN}Press Ctrl+C to stop all services${NC}"

# Keep script running
wait
