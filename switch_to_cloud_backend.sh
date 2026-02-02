#!/bin/bash

# Script to switch Android app to use cloud backend server

set -e

CLOUD_SERVER="http://81.70.234.241:8080/"

echo "🔄 Switching Android app to use cloud backend server ($CLOUD_SERVER)..."

# Update the API client to use cloud server
if [ -f "~/gongwuyuan-app/frontend/app/src/main/java/com/gwy/exam/api/ApiClient.java" ]; then
    sed -i "s|http://10.0.2.2:8080/|$CLOUD_SERVER|g" ~/gongwuyuan-app/frontend/app/src/main/java/com/gwy/exam/api/ApiClient.java
    echo "✅ Updated Android app to use cloud backend ($CLOUD_SERVER)"
else
    echo "❌ Android app API client not found at expected location"
    exit 1
fi

echo ""
echo "📋 Android app now configured to use cloud server:"
echo "   API Endpoint: $CLOUD_SERVER"
echo ""
echo "✅ Ready for cloud deployment testing"