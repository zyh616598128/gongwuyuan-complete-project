#!/bin/bash

# Script to start the Gongwuyuan backend service on cloud server
# This script assumes you have SSH access to the server

SERVER_IP="81.70.234.241"
SSH_KEY="/home/zhuyinhang/tenxunyunfuwuqimiyao/beijinshili.pem"
USERNAME="root"

echo "🚀 Starting Gongwuyuan backend service on $SERVER_IP..."

# Function to execute command on remote server
execute_remote() {
    ssh -i "$SSH_KEY" "$USERNAME@$SERVER_IP" "$1"
}

# Check if we can connect to the server
echo "🌐 Testing SSH connectivity..."
if ssh -i "$SSH_KEY" -o ConnectTimeout=10 -o BatchMode=yes "$USERNAME@$SERVER_IP" exit 2>/dev/null; then
    echo "✅ Successfully connected to server"
else
    echo "❌ Cannot connect to server. Please verify:"
    echo "   - SSH key is correct and has proper permissions"
    echo "   - Server IP is accessible"
    echo "   - Network connectivity to server"
    exit 1
fi

echo ""
echo "🔍 Checking if service is already running..."
SERVICE_RUNNING=$(execute_remote "ps aux | grep gongwuyuan || ps aux | grep java | grep -v grep")

if [ -n "$SERVICE_RUNNING" ]; then
    echo "✅ Service appears to be running:"
    echo "$SERVICE_RUNNING"
    echo ""
    PORT_CHECK=$(execute_remote "netstat -tlnp | grep 8080 || ss -tlnp | grep 8080")
    if [ -n "$PORT_CHECK" ]; then
        echo "✅ Service is listening on port 8080"
        echo "🔗 Backend API should be available at http://$SERVER_IP:8080"
        exit 0
    else
        echo "⚠️  Service process found but not listening on port 8080"
        echo "💡 Consider restarting the service"
    fi
else
    echo "❌ Service is not running"
fi

echo ""
echo "📁 Looking for application JAR files..."
JAR_FILES=$(execute_remote "find / -name '*.jar' -path '*/gongwuyuan*' -o -name '*gongwuyuan*.jar' 2>/dev/null | head -5")

if [ -n "$JAR_FILES" ]; then
    echo "📦 Found potential application JAR files:"
    echo "$JAR_FILES"
    echo ""
    
    # Find the most recent JAR file
    APP_JAR=$(execute_remote "find / -name '*gongwuyuan*.jar' 2>/dev/null | head -1")
    
    if [ -n "$APP_JAR" ] && [ "$APP_JAR" != "" ]; then
        echo "🎯 Found application JAR: $APP_JAR"
        echo "🚀 Attempting to start service..."
        
        START_CMD="nohup java -jar '$APP_JAR' --server.port=8080 > /var/log/gongwuyuan-app.log 2>&1 &"
        execute_remote "$START_CMD"
        
        echo "⏳ Waiting 10 seconds for service to start..."
        sleep 10
        
        # Check if service is now running
        PORT_CHECK=$(execute_remote "netstat -tlnp | grep 8080 || ss -tlnp | grep 8080")
        if [ -n "$PORT_CHECK" ]; then
            echo "✅ Service is now running and listening on port 8080"
            echo "🔗 Backend API should be available at http://$SERVER_IP:8080"
        else
            echo "❌ Service may not have started properly"
            echo "📋 Check logs: ssh -i $SSH_KEY $USERNAME@$SERVER_IP 'tail -50 /var/log/gongwuyuan-app.log'"
        fi
    else
        echo "❌ Could not find application JAR file"
        echo "💡 Manual intervention required to locate and start the application"
    fi
else
    echo "❌ No application JAR files found"
    echo "💡 Application may need to be deployed first"
fi

echo ""
echo "📋 To manually check service status, run:"
echo "ssh -i $SSH_KEY $USERNAME@$SERVER_IP"
echo "ps aux | grep java"
echo "netstat -tlnp | grep 8080"