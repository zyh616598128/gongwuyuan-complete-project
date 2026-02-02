#!/bin/bash

# Gongwuyuan App - Local Backend Deployment Script
# Sets up the Spring Boot backend locally for development

set -e  # Exit on any error

echo "🚀 Starting Gongwuyuan App Local Backend Deployment..."

# Create project directory structure
mkdir -p ~/gongwuyuan-local/{mysql-data,redis-data,logs}

# Start MySQL container
echo "📦 Starting MySQL container..."
docker run -d \
  --name gongwuyuan-mysql \
  -e MYSQL_DATABASE=gongwuyuan_db \
  -e MYSQL_USER=gongwuyuan_user \
  -e MYSQL_PASSWORD=SecurePass123 \
  -e MYSQL_ROOT_PASSWORD=root123 \
  -p 3306:3306 \
  -v ~/gongwuyuan-local/mysql-data:/var/lib/mysql \
  -v ~/gongwuyuan-local/logs:/var/log/mysql \
  mysql:8.0

# Start Redis container
echo "⚡ Starting Redis container..."
docker run -d \
  --name gongwuyuan-redis \
  -p 6379:6379 \
  -v ~/gongwuyuan-local/redis-data:/data \
  redis:alpine \
  redis-server --appendonly yes

# Wait for MySQL to be ready
echo "⏳ Waiting for MySQL to start..."
sleep 30

# Clone the backend code if not already present
if [ ! -d "~/gongwuyuan-backend" ]; then
    echo "📥 Cloning backend repository..."
    git clone https://github.com/zyh616598128/gongwuyuan-complete-project.git ~/gongwuyuan-backend
    cd ~/gongwuyuan-backend/backend
else
    echo "🔄 Updating backend repository..."
    cd ~/gongwuyuan-backend/backend
    git pull
fi

# Build the backend
echo "🔨 Building backend application..."
cd ~/gongwuyuan-backend/backend
./mvnw clean package -DskipTests

# Run the Spring Boot application
echo "🏃‍♂️ Starting backend application..."
nohup java -jar target/gongwuyuan-0.0.1-SNAPSHOT.jar > ~/gongwuyuan-local/logs/backend.log 2>&1 &

# Wait for the backend to start
echo "⏳ Waiting for backend to start..."
sleep 20

# Update the Android app to use local backend
echo "📱 Updating Android app to use local backend..."
if [ -f "~/gongwuyuan-app/frontend/app/src/main/java/com/gwy/exam/api/ApiClient.java" ]; then
    sed -i 's|http://81.70.234.241:8080/|http://10.0.2.2:8080/|g' ~/gongwuyuan-app/frontend/app/src/main/java/com/gwy/exam/api/ApiClient.java
    echo "✅ Updated Android app to use local backend (http://10.0.2.2:8080)"
else
    echo "⚠️ Android app not found, please ensure it's located at ~/gongwuyuan-app/"
fi

echo ""
echo "🎉 Local deployment completed!"
echo ""
echo "📊 Services running:"
echo "   MySQL: localhost:3306"
echo "   Redis: localhost:6379" 
echo "   Backend API: http://localhost:8080"
echo "   Android App (via emulator): http://10.0.2.2:8080"
echo ""
echo "📋 To access the backend API directly:"
echo "   Health: curl http://localhost:8080/health"
echo "   Example: curl http://localhost:8080/api/questions"
echo ""
echo "💾 Data is persisted in ~/gongwuyuan-local/"
echo ""
echo "🔧 To stop services: docker stop gongwuyuan-mysql gongwuyuan-redis"
echo "🔧 To start again: docker start gongwuyuan-mysql gongwuyuan-redis"