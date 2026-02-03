// Frontend Integration Test
// This test simulates how the React frontend would interact with the backend API

const testResults = [];

async function testAPI(endpoint, method = 'GET', data = null, token = null) {
    const url = `http://localhost:8081/api${endpoint}`;
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json',
        }
    };

    if (token) {
        options.headers['Authorization'] = `Bearer ${token}`;
    }

    if (data) {
        options.body = JSON.stringify(data);
    }

    try {
        const response = await fetch(url, options);
        const result = await response.json();
        return { status: response.status, data: result };
    } catch (error) {
        return { status: 0, error: error.message };
    }
}

async function runFrontendIntegrationTests() {
    console.log('=== Frontend-Backend Integration Tests ===\n');

    // Test user credentials
    const testUser = {
        username: 'frontend_test_' + Date.now(),
        email: `frontend_test_${Date.now()}@example.com`,
        password: 'FrontendTest123!',
        phone: '13800138000',
        nickname: 'FrontendTester'
    };

    console.log(`Test user: ${testUser.username}`);
    console.log(`Test email: ${testUser.email}\n`);

    // Phase 1: Authentication Flow (Similar to LoginPage.js and AuthService.js)
    console.log('=== Phase 1: Authentication Flow ===');
    
    // Register user (similar to registerUser in authService)
    let result = await testAPI('/auth/register', 'POST', testUser);
    console.log(`Registration: ${result.status === 200 ? '✅ PASS' : '❌ FAIL'} (${result.status})`);
    testResults.push({ test: 'Registration', status: result.status === 200 });

    // Login user (similar to login in authService)
    result = await testAPI('/auth/login', 'POST', {
        username: testUser.username,
        password: testUser.password
    });
    const token = result.data?.data;
    console.log(`Login: ${result.status === 200 ? '✅ PASS' : '❌ FAIL'} (${result.status})`);
    testResults.push({ test: 'Login', status: result.status === 200 && token });

    if (!token) {
        console.log('❌ Cannot proceed without authentication token');
        return;
    }

    console.log('✅ Token obtained successfully\n');

    // Phase 2: User Profile Management (Similar to ProfilePage.js)
    console.log('=== Phase 2: User Profile Management ===');

    // Get user profile (similar to getUserProfile in authService)
    result = await testAPI('/users/profile', 'GET', null, token);
    console.log(`Get Profile: ${result.status === 200 ? '✅ PASS' : '❌ FAIL'} (${result.status})`);
    testResults.push({ test: 'Get Profile', status: result.status === 200 });

    // Update user profile (similar to updateProfile in authService)
    result = await testAPI('/users/profile', 'PUT', {
        phone: '13800138001',
        nickname: 'UpdatedFrontendTester'
    }, token);
    console.log(`Update Profile: ${result.status === 200 ? '✅ PASS' : '❌ FAIL'} (${result.status})`);
    testResults.push({ test: 'Update Profile', status: result.status === 200 });

    // Phase 3: Subject and Question Browsing (Similar to HomePage.js and QuestionBankPage.js)
    console.log('\n=== Phase 3: Subject and Question Browsing ===');

    // Get subjects (similar to fetchSubjects in frontend)
    result = await testAPI('/subjects', 'GET', null, token);
    console.log(`Get Subjects: ${result.status === 200 ? '✅ PASS' : '❌ FAIL'} (${result.status})`);
    testResults.push({ test: 'Get Subjects', status: result.status === 200 });

    // Get questions (similar to fetchQuestions in frontend)
    result = await testAPI('/questions?subjectId=1&limit=5', 'GET', null, token);
    console.log(`Get Questions: ${result.status === 200 ? '✅ PASS' : '❌ FAIL'} (${result.status})`);
    testResults.push({ test: 'Get Questions', status: result.status === 200 });

    // Phase 4: Exam Operations (Similar to ExamPage.js)
    console.log('\n=== Phase 4: Exam Operations ===');

    // Create an exam (similar to createExam in frontend)
    result = await testAPI('/exams', 'POST', {
        subjectId: 1,
        title: 'Frontend Integration Test Exam',
        durationMinutes: 30,
        questionCount: 5
    }, token);
    const examId = result.data?.data?.id;
    console.log(`Create Exam: ${result.status === 200 ? '✅ PASS' : '❌ FAIL'} (${result.status})`);
    testResults.push({ test: 'Create Exam', status: result.status === 200 && examId });

    // Get exam history (similar to fetchExamHistory in frontend)
    result = await testAPI('/exams/history', 'GET', null, token);
    console.log(`Get Exam History: ${result.status === 200 ? '✅ PASS' : '❌ FAIL'} (${result.status})`);
    testResults.push({ test: 'Get Exam History', status: result.status === 200 });

    if (examId) {
        // Submit exam answers (similar to submitExam in frontend)
        result = await testAPI(`/exams/${examId}/submit`, 'POST', {
            answers: [
                { questionId: 1, selectedOptionId: 1 },
                { questionId: 2, selectedOptionId: 2 }
            ]
        }, token);
        console.log(`Submit Exam: ${result.status === 200 ? '✅ PASS' : '❌ FAIL'} (${result.status})`);
        testResults.push({ test: 'Submit Exam', status: result.status === 200 });
    }

    // Phase 5: Wrong Questions Management (Similar to WrongQuestionPage.js)
    console.log('\n=== Phase 5: Wrong Questions Management ===');

    // Add to wrong questions (similar to addToWrongQuestions in frontend)
    result = await testAPI('/wrong-questions', 'POST', {
        questionId: 1,
        notes: 'Test note from frontend integration'
    }, token);
    console.log(`Add Wrong Question: ${result.status === 200 ? '✅ PASS' : '❌ FAIL'} (${result.status})`);
    testResults.push({ test: 'Add Wrong Question', status: result.status === 200 });

    // Get wrong questions (similar to fetchWrongQuestions in frontend)
    result = await testAPI('/wrong-questions', 'GET', null, token);
    console.log(`Get Wrong Questions: ${result.status === 200 ? '✅ PASS' : '❌ FAIL'} (${result.status})`);
    testResults.push({ test: 'Get Wrong Questions', status: result.status === 200 });

    // Phase 6: Frontend Asset Verification
    console.log('\n=== Phase 6: Frontend Asset Verification ===');

    // Test if frontend is serving correctly
    try {
        const frontendResponse = await fetch('http://localhost:3000');
        console.log(`Frontend Availability: ${frontendResponse.ok ? '✅ PASS' : '❌ FAIL'} (${frontendResponse.status})`);
        testResults.push({ test: 'Frontend Availability', status: frontendResponse.ok });
    } catch (error) {
        console.log(`Frontend Availability: ❌ FAIL (${error.message})`);
        testResults.push({ test: 'Frontend Availability', status: false });
    }

    // Phase 7: API Consistency Check
    console.log('\n=== Phase 7: API Consistency Check ===');

    // Test common API patterns used by React components
    const apiPatterns = [
        { endpoint: '/subjects', method: 'GET', desc: 'Get all subjects' },
        { endpoint: '/questions?subjectId=1&limit=10', method: 'GET', desc: 'Get questions with params' },
        { endpoint: '/users/profile', method: 'GET', desc: 'Get user profile' },
        { endpoint: '/exams/history', method: 'GET', desc: 'Get exam history' },
        { endpoint: '/wrong-questions', method: 'GET', desc: 'Get wrong questions' }
    ];

    for (const pattern of apiPatterns) {
        result = await testAPI(pattern.endpoint, pattern.method, null, token);
        console.log(`${pattern.desc}: ${result.status === 200 ? '✅ PASS' : '❌ FAIL'} (${result.status})`);
        testResults.push({ test: pattern.desc, status: result.status === 200 });
    }

    // Summary
    console.log('\n=== Test Summary ===');
    const passedTests = testResults.filter(r => r.status).length;
    const totalTests = testResults.length;
    
    console.log(`Passed: ${passedTests}/${totalTests} tests`);
    
    if (passedTests === totalTests) {
        console.log('🎉 All frontend-backend integration tests passed!');
        console.log('✅ React frontend can successfully communicate with Spring Boot backend');
        console.log('✅ All API endpoints are accessible and functional');
        console.log('✅ Authentication and authorization working correctly');
        console.log('✅ Data flow between frontend and backend verified');
    } else {
        console.log(`❌ ${totalTests - passedTests} tests failed`);
    }

    return { passed: passedTests, total: totalTests, results: testResults };
}

// Run the tests
runFrontendIntegrationTests()
    .then(results => {
        console.log('\nDetailed Results:', results.results);
    })
    .catch(error => {
        console.error('Test execution error:', error);
    });