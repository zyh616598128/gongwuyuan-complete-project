// Test script to verify registration functionality
const axios = require('axios');

async function testRegistration() {
    console.log('Testing registration functionality...\n');
    
    // Test data
    const userData = {
        username: 'test_user_' + Date.now(),
        email: `test_${Date.now()}@example.com`,
        password: 'TestPassword123!',
        phone: '13800138000',
        nickname: 'Test User'
    };
    
    console.log('Registering user:', userData.username);
    
    try {
        // Test registration
        const registerResponse = await axios.post('http://localhost:8081/api/auth/register', userData);
        console.log('✅ Registration Response:', registerResponse.data);
        
        if (registerResponse.data.success) {
            console.log('✅ Registration successful');
            
            // Test login
            console.log('\nTesting login...');
            const loginResponse = await axios.post('http://localhost:8081/api/auth/login', {
                username: userData.username,
                password: userData.password
            });
            
            console.log('✅ Login Response:', loginResponse.data);
            
            if (loginResponse.data.success && loginResponse.data.data) {
                console.log('✅ Login successful');
                
                // Test accessing protected endpoint
                console.log('\nTesting protected endpoint access...');
                const profileResponse = await axios.get('http://localhost:8081/api/users/profile', {
                    headers: {
                        'Authorization': `Bearer ${loginResponse.data.data}`
                    }
                });
                
                console.log('✅ Profile Response:', profileResponse.data);
                
                if (profileResponse.data.success) {
                    console.log('✅ Profile access successful');
                    console.log('\n🎉 All tests passed! Registration and authentication flow working correctly.');
                } else {
                    console.log('❌ Profile access failed');
                }
            } else {
                console.log('❌ Login failed');
            }
        } else {
            console.log('❌ Registration failed');
        }
    } catch (error) {
        console.error('❌ Error occurred:', error.response?.data || error.message);
    }
}

// Run the test
testRegistration();