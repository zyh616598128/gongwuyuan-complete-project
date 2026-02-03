#!/bin/bash
# Script to switch between cloud and local backend configurations

echo "Gongwuyuan App - Backend Configuration Switcher"
echo "Current API Client configuration:"
grep -A 1 "BASE_URL" /home/zhuyinhang/.openclaw/workspace/gongwuyuan-app/frontend/app/src/main/java/com/gwy/exam/api/ApiClient.java

echo ""
echo "Options:"
echo "1) Switch to LOCAL backend (http://10.0.2.2:8081) - for Android emulator"
echo "2) Switch to CLOUD backend (http://81.70.234.241:8080) - for production"
echo "3) Switch to CUSTOM backend - enter your own URL"
echo ""

read -p "Enter your choice (1/2/3): " choice

case $choice in
    1)
        sed -i 's|private static final String BASE_URL =.*|private static final String BASE_URL = "http://10.0.2.2:8081/"; // 本地后端地址 (for Android emulator)|' /home/zhuyinhang/.openclaw/workspace/gongwuyuan-app/frontend/app/src/main/java/com/gwy/exam/api/ApiClient.java
        echo "✅ Switched to LOCAL backend configuration"
        ;;
    2)
        sed -i 's|private static final String BASE_URL =.*|private static final String BASE_URL = "http://81.70.234.241:8080/"; // 云服务器地址|' /home/zhuyinhang/.openclaw/workspace/gongwuyuan-app/frontend/app/src/main/java/com/gwy/exam/api/ApiClient.java
        echo "✅ Switched to CLOUD backend configuration"
        ;;
    3)
        read -p "Enter your custom backend URL (include trailing slash): " custom_url
        sed -i "s|private static final String BASE_URL =.*|private static final String BASE_URL = \"$custom_url\"; // 自定义后端地址|" /home/zhuyinhang/.openclaw/workspace/gongwuyuan-app/frontend/app/src/main/java/com/gwy/exam/api/ApiClient.java
        echo "✅ Switched to CUSTOM backend configuration"
        ;;
    *)
        echo "❌ Invalid choice"
        exit 1
        ;;
esac

echo ""
echo "New API Client configuration:"
grep -A 1 "BASE_URL" /home/zhuyinhang/.openclaw/workspace/gongwuyuan-app/frontend/app/src/main/java/com/gwy/exam/api/ApiClient.java
echo ""
echo "💡 Remember to rebuild the Android project for changes to take effect!"