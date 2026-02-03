#!/bin/bash

echo "Starting Gongwuyuan Application Deployment..."

# Start backend service
echo "Starting backend service..."
cd ~/.openclaw/workspace/gongwuyuan-app/backend
nohup java -jar -Dspring.profiles.active=local -Dserver.port=8081 target/gwy-backend-1.0.0.jar > ../logs/gwy-backend-console.log 2>&1 &

echo "Waiting for backend to start..."
sleep 15

# Start frontend service
echo "Starting frontend service..."
cd ~/.openclaw/workspace/gongwuyuan-app/frontend/web
npx serve -s build > serve.log 2>&1 &

echo "Services started!"
echo "Backend: http://localhost:8081"
echo "Frontend: http://localhost:3000"
echo "Deployment completed successfully!"