#!/bin/bash

# Script to check the status of Gongwuyuan backend service on cloud server
# This script assumes you have SSH access to the server

SERVER_IP="81.70.234.241"
SSH_KEY="/home/zhuyinhang/tenxunyunfuwuqimiyao/beijinshili.pem"
USERNAME="root"

echo "🔍 Checking Gongwuyuan backend service status on $SERVER_IP..."

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
echo "📊 Checking system status..."
execute_remote "uptime"
echo ""

echo "🔍 Checking if Java processes are running..."
execute_remote "ps aux | grep java | grep -v grep"
echo ""

echo "🔌 Checking if port 8080 is in use..."
execute_remote "netstat -tlnp | grep 8080 || ss -tlnp | grep 8080"
echo ""

echo "💾 Checking disk usage..."
execute_remote "df -h"
echo ""

echo "📈 Checking memory usage..."
execute_remote "free -h"
echo ""

echo "📋 Checking recent application logs (if available)..."
execute_remote "find / -name '*gongwuyuan*' -type f -path '*/log*' 2>/dev/null | head -10"
echo ""

echo "🔧 If the service is not running, you can start it with:"
echo "   ssh -i $SSH_KEY $USERNAME@$SERVER_IP"
echo "   # Then run the application, for example:"
echo "   # nohup java -jar /path/to/gongwuyuan-app.jar > /var/log/gongwuyuan-app.log 2>&1 &"
echo ""

echo "✅ Server status check completed!"