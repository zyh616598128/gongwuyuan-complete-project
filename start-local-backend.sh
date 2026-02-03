#!/bin/bash
# Startup script for Gongwuyuan App local backend

echo "Starting Gongwuyuan App local backend..."
echo "Using H2 in-memory database for local development"
echo "Access the application at: http://localhost:8081"
echo "Access H2 console at: http://localhost:8081/h2-console"

cd /home/zhuyinhang/.openclaw/workspace/gongwuyuan-app/backend

# Kill any existing process on port 8081
lsof -ti:8081 | xargs kill -9 2>/dev/null || true

# Start the backend
java -jar -Dspring.profiles.active=local -Dserver.port=8081 target/gwy-backend-1.0.0.jar