#!/bin/bash

# Cleanup script for Gongwuyuan Application
# Removes build artifacts and temporary files

echo "Cleaning up Gongwuyuan application build artifacts..."

# Remove backend build artifacts
echo "Removing backend build artifacts..."
rm -rf backend/target/
rm -f backend/*.jar
rm -f backend/*.war

# Remove frontend build artifacts  
echo "Removing frontend build artifacts..."
rm -rf frontend/web/build/
rm -f frontend/web/*.log
rm -rf frontend/web/node_modules/

# Remove temporary files
echo "Removing temporary files..."
rm -f *.log
rm -rf logs/
rm -f tmp/
rm -rf temp/

# Remove any remaining large files that shouldn't be in repo
find . -name "*.jar" -not -path "./.git/*" -delete 2>/dev/null || true
find . -name "*.war" -not -path "./.git/*" -delete 2>/dev/null || true
find . -name "*.class" -not -path "./.git/*" -delete 2>/dev/null || true

echo "Cleanup completed successfully!"
echo ""
echo "Repository is now clean of build artifacts."
echo "Remember: Only source code should be committed to the repository."
echo "Build artifacts are created during deployment process."