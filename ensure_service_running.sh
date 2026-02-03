#!/bin/bash

# Script to ensure Gongwuyuan backend service is running on cloud server
# This script checks service status and starts it if needed

SERVER_IP="81.70.234.241"
SSH_KEY="/home/zhuyinhang/tenxunyunfuwuqimiyao/beijinshili.pem"
USERNAME="root"

echo "🔍 Checking Gongwuyuan backend service on $SERVER_IP..."

# Test SSH connectivity
if ! ssh -i "$SSH_KEY" -o ConnectTimeout=10 -o BatchMode=yes "$USERNAME@$SERVER_IP" exit 2>/dev/null; then
    echo "❌ Cannot connect to server. Please verify SSH access."
    exit 1
fi

echo "✅ Server is accessible via SSH"

# Check if service is running on port 8080
IS_RUNNING=$(ssh -i "$SSH_KEY" "$USERNAME@$SERVER_IP" "netstat -tlnp | grep 8080 || ss -tlnp | grep 8080" 2>/dev/null)

if [ -n "$IS_RUNNING" ]; then
    echo "✅ Service is already running on port 8080"
    echo "🔗 API should be accessible at http://$SERVER_IP:8080"
else
    echo "❌ Service is NOT running on port 8080"
    echo "🚀 Attempting to start the service..."
    
    # Try to find and start the application
    JAR_FILE=$(ssh -i "$SSH_KEY" "$USERNAME@$SERVER_IP" "find /root -name '*gongwuyuan*.jar' 2>/dev/null | head -1")
    
    if [ -n "$JAR_FILE" ] && [ "$JAR_FILE" != "" ]; then
        echo "📦 Found application JAR: $JAR_FILE"
        echo "🚀 Starting application..."
        
        # Start the application
        ssh -i "$SSH_KEY" "$USERNAME@$SERVER_IP" "nohup java -jar '$JAR_FILE' --server.port=8080 > /var/log/gongwuyuan-app.log 2>&1 &" 2>/dev/null
        
        # Wait for service to start
        echo "⏳ Waiting 15 seconds for service to start..."
        sleep 15
        
        # Verify service is now running
        IS_RUNNING_AFTER=$(ssh -i "$SSH_KEY" "$USERNAME@$SERVER_IP" "netstat -tlnp | grep 8080 || ss -tlnp | grep 8080" 2>/dev/null)
        
        if [ -n "$IS_RUNNING_AFTER" ]; then
            echo "✅ Service has been successfully started on port 8080"
            echo "🔗 API should now be accessible at http://$SERVER_IP:8080"
            
            # Test the health endpoint
            HEALTH_STATUS=$(ssh -i "$SSH_KEY" "$USERNAME@$SERVER_IP" "curl -s --connect-timeout 5 http://localhost:8080/health" 2>/dev/null || echo "Health check failed")
            if [[ "$HEALTH_STATUS" == *"UP"* ]] || [[ "$HEALTH_STATUS" == *"status"* ]]; then
                echo "💚 Health check passed: Service is operational"
            else
                echo "⚠️  Health check inconclusive, but service is listening on port 8080"
            fi
        else
            echo "❌ Service failed to start properly"
            echo "📋 Check server logs for more information:"
            echo "   ssh -i $SSH_KEY $USERNAME@$SERVER_IP 'tail -50 /var/log/gongwuyuan-app.log'"
        fi
    else
        echo "❌ Could not find application JAR file"
        echo "💡 The application may need to be deployed first"
        echo "📋 Available JAR files on server:"
        ssh -i "$SSH_KEY" "$USERNAME@$SERVER_IP" "find /root -name '*.jar' 2>/dev/null"
    fi
fi

echo ""
echo "📋 To verify service status manually:"
echo "ssh -i $SSH_KEY $USERNAME@$SERVER_IP 'netstat -tlnp | grep 8080'"
echo ""
echo "🔗 To test API endpoint:"
echo "curl http://$SERVER_IP:8080/health"