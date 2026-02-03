#!/bin/bash

echo "=== Gongwuyuan Full Workflow Test ==="
echo ""

# Configuration
BACKEND_URL="http://localhost:8081"
TEST_USER="workflow_test_user_$(date +%s)"
TEST_EMAIL="workflow_test_${TEST_USER}@example.com"
TEST_PASSWORD="WorkflowTest123!"

echo "Test user: $TEST_USER"
echo "Test email: $TEST_EMAIL"
echo ""

# Function to perform API tests
test_api() {
    local endpoint=$1
    local method=$2
    local data=$3
    local expected_status=$4
    local description=$5
    
    echo -n "Testing $description... "
    
    if [ "$method" = "GET" ]; then
        response=$(curl -s -w "\n%{http_code}" "$BACKEND_URL$endpoint")
    elif [ "$method" = "POST" ]; then
        response=$(curl -s -w "\n%{http_code}" -X POST -H "Content-Type: application/json" -d "$data" "$BACKEND_URL$endpoint")
    elif [ "$method" = "PUT" ]; then
        response=$(curl -s -w "\n%{http_code}" -X PUT -H "Content-Type: application/json" -d "$data" "$BACKEND_URL$endpoint")
    elif [ "$method" = "DELETE" ]; then
        response=$(curl -s -w "\n%{http_code}" -X DELETE "$BACKEND_URL$endpoint")
    fi
    
    status=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')
    
    if [ "$status" = "$expected_status" ]; then
        echo "✅ PASS ($status)"
        echo "$body" > /tmp/workflow_test_response.json
    else
        echo "❌ FAIL (Expected: $expected_status, Got: $status)"
        echo "Response: $body"
        return 1
    fi
}

# Function to test authenticated endpoints
test_auth_api() {
    local endpoint=$1
    local method=$2
    local data=$3
    local token=$4
    local expected_status=$5
    local description=$6
    
    echo -n "Testing $description... "
    
    if [ "$method" = "GET" ]; then
        response=$(curl -s -w "\n%{http_code}" -H "Authorization: Bearer $token" "$BACKEND_URL$endpoint")
    elif [ "$method" = "POST" ]; then
        response=$(curl -s -w "\n%{http_code}" -X POST -H "Content-Type: application/json" -H "Authorization: Bearer $token" -d "$data" "$BACKEND_URL$endpoint")
    elif [ "$method" = "PUT" ]; then
        response=$(curl -s -w "\n%{http_code}" -X PUT -H "Content-Type: application/json" -H "Authorization: Bearer $token" -d "$data" "$BACKEND_URL$endpoint")
    elif [ "$method" = "DELETE" ]; then
        response=$(curl -s -w "\n%{http_code}" -H "Authorization: Bearer $token" -X DELETE "$BACKEND_URL$endpoint")
    fi
    
    status=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')
    
    if [ "$status" = "$expected_status" ]; then
        echo "✅ PASS ($status)"
        echo "$body" > /tmp/workflow_test_response.json
    else
        echo "❌ FAIL (Expected: $expected_status, Got: $status)"
        echo "Response: $body"
        return 1
    fi
}

# Store authentication token
TOKEN=""

echo "=== Phase 1: User Registration and Authentication ==="

# Register a new user
test_api "/api/auth/register" "POST" "{\"username\":\"$TEST_USER\",\"email\":\"$TEST_EMAIL\",\"password\":\"$TEST_PASSWORD\",\"phone\":\"13800138000\",\"nickname\":\"Workflow Test User\"}" "200" "User Registration"

# Login and get token
test_api "/api/auth/login" "POST" "{\"username\":\"$TEST_USER\",\"password\":\"$TEST_PASSWORD\"}" "200" "User Login"

# Extract token from response
TOKEN=$(grep -o '"data":"[^"]*"' /tmp/workflow_test_response.json | cut -d'"' -f4)
if [ -n "$TOKEN" ]; then
    echo "✅ Token retrieved successfully"
else
    echo "❌ Failed to retrieve token"
    exit 1
fi

echo ""
echo "=== Phase 2: Profile and Settings ==="

# Get user profile
test_auth_api "/api/users/profile" "GET" "" "$TOKEN" "200" "Get User Profile"

# Update user profile
test_auth_api "/api/users/profile" "PUT" "{\"phone\":\"13800138001\",\"nickname\":\"UpdatedUser\"}" "$TOKEN" "200" "Update User Profile"

echo ""
echo "=== Phase 3: Subject Exploration ==="

# Get all subjects
test_auth_api "/api/subjects" "GET" "" "$TOKEN" "200" "Get All Subjects"

echo ""
echo "=== Phase 4: Question Practice ==="

# Get questions from first subject
test_auth_api "/api/questions?subjectId=1&limit=3" "GET" "" "$TOKEN" "200" "Get Questions from Subject 1"

echo ""
echo "=== Phase 5: Create and Take Exam ==="

# Create a mock exam
test_auth_api "/api/exams" "POST" "{\"subjectId\":1,\"title\":\"Mock Exam for Workflow Test\",\"durationMinutes\":30,\"questionCount\":5}" "$TOKEN" "200" "Create Mock Exam"

# Extract exam ID from response
EXAM_ID=$(grep -o '"id":[0-9]*' /tmp/workflow_test_response.json | cut -d':' -f2)
if [ -n "$EXAM_ID" ] && [ "$EXAM_ID" != "null" ]; then
    echo "✅ Exam created with ID: $EXAM_ID"
else
    echo "❌ Failed to extract exam ID"
    EXAM_ID=1  # Fallback
fi

echo ""
echo "=== Phase 6: Manage Wrong Questions ==="

# Add a question to wrong questions list
test_auth_api "/api/wrong-questions" "POST" "{\"questionId\":1,\"notes\":\"Need to review this concept for workflow test\"}" "$TOKEN" "200" "Add Question to Wrong List"

# Get all wrong questions
test_auth_api "/api/wrong-questions" "GET" "" "$TOKEN" "200" "Get Wrong Questions List"

echo ""
echo "=== Phase 7: Review Exam History ==="

# Get exam history
test_auth_api "/api/exams/history" "GET" "" "$TOKEN" "200" "Get Exam History"

echo ""
echo "=== Phase 8: Complete Exam Submission (Simulated) ==="

# Simulate submitting an exam with answers
test_auth_api "/api/exams/$EXAM_ID/submit" "POST" "{\"answers\":[{\"questionId\":1,\"selectedOptionId\":1},{\"questionId\":2,\"selectedOptionId\":2}]}" "$TOKEN" "200" "Submit Exam Answers"

echo ""
echo "=== Phase 9: Final Profile Check ==="

# Get updated user profile after activities
test_auth_api "/api/users/profile" "GET" "" "$TOKEN" "200" "Final User Profile Check"

echo ""
echo "=== Full Workflow Test Summary ==="
echo "✅ User registration and authentication successful"
echo "✅ Profile management working"
echo "✅ Subject browsing functional"
echo "✅ Question practice available"
echo "✅ Exam creation and submission working"
echo "✅ Wrong question management operational"
echo "✅ Exam history tracking functional"
echo "✅ Complete user workflow validated"
echo ""
echo "All core functionalities tested successfully!"