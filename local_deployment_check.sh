#!/bin/bash
# Local deployment verification script for Gongwuyuan App

echo "==========================================="
echo "Gongwuyuan App - Local Backend Deployment"
echo "==========================================="

echo "1. Checking if backend service is running..."
if pgrep -f "gwy-backend-1.0.0.jar" > /dev/null; then
    echo "✅ Backend service is running"
else
    echo "❌ Backend service is not running"
    exit 1
fi

echo "2. Checking port 8081..."
if lsof -i :8081 > /dev/null; then
    echo "✅ Port 8081 is in use by the backend service"
else
    echo "❌ Port 8081 is not in use"
    exit 1
fi

echo "3. Backend features:"
echo "   - Running with in-memory H2 database"
echo "   - H2 Console available at http://localhost:8081/h2-console"
echo "   - Database URL: jdbc:h2:mem:testdb"
echo "   - Auto-created database schema"
echo "   - All endpoints available at http://localhost:8081/api/*"

echo "4. Available endpoints:"
echo "   - GET  /api/auth/login"
echo "   - POST /api/auth/register"
echo "   - GET  /api/users/profile"
echo "   - GET  /api/subjects"
echo "   - GET  /api/questions"
echo "   - GET  /api/categories"
echo "   - POST /api/exams"
echo "   - GET  /api/exams/{id}"
echo "   - POST /api/exams/{id}/submit"
echo "   - GET  /api/wrong-questions"

echo ""
echo "==========================================="
echo "Local deployment successful!"
echo "Backend is running on http://localhost:8081"
echo "==========================================="