#!/bin/bash
# Gongwuyuan Project Helper Script

PROJECT_ROOT="/home/zhuyinhang/.openclaw/workspace/gongwuyuan-app"

# Function to check project status
check_status() {
    echo "=== Gongwuyuan Project Status ==="
    cd $PROJECT_ROOT
    
    echo "Git Status:"
    git status --short
    
    echo -e "\nBranch Info:"
    git branch --show-current
    
    echo -e "\nLast Commit:"
    git log -1 --oneline
    
    echo -e "\nUncommitted Changes:"
    git diff --stat
}

# Function to update project
update_project() {
    echo "Updating Gongwuyuan project..."
    cd $PROJECT_ROOT
    git pull origin main
    echo "Project updated successfully!"
}

# Function to show project documentation
show_docs() {
    echo "Available Documentation:"
    ls -la $PROJECT_ROOT/*.md
}

# Function to run project checks
run_checks() {
    echo "Running project checks..."
    cd $PROJECT_ROOT
    
    # Check if all required documentation exists
    for doc in "requirements-document.md" "api-contract.md" "web-technical-spec.md" "development-guidelines.md"; do
        if [ -f "$doc" ]; then
            echo "✓ $doc exists"
        else
            echo "✗ $doc missing"
        fi
    done
    
    # Check backend
    if [ -d "backend" ]; then
        echo "✓ Backend directory exists"
    else
        echo "✗ Backend directory missing"
    fi
    
    # Check frontend
    if [ -d "frontend" ]; then
        echo "✓ Frontend directory exists"
    else
        echo "✗ Frontend directory missing"
    fi
}

case "$1" in
    "status")
        check_status
        ;;
    "update")
        update_project
        ;;
    "docs")
        show_docs
        ;;
    "check")
        run_checks
        ;;
    *)
        echo "Usage: $0 {status|update|docs|check}"
        echo "  status - Show project status"
        echo "  update - Update project from main branch"
        echo "  docs - Show available documentation"
        echo "  check - Run project health checks"
        ;;
esac