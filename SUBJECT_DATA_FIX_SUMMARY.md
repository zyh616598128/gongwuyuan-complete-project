# Gongwuyuan Application - Subject Data Display Fix

## Issue Identified
Frontend displayed "暂无可用科目，请联系管理员添加科目信息" (No subjects available, please contact admin) despite backend having subject data.

## Root Cause Analysis
The issue was caused by incorrect API calls in frontend components:
1. **Incorrect API Instance**: Using direct `axios` import instead of configured `api` instance
2. **Missing API Path Prefix**: Omitting `/api` prefix in API calls
3. **Configuration Mismatch**: Frontend components not using proper backend API configuration

## Solution Applied
Fixed all affected frontend pages by:

### PracticePage.js
- Changed `import axios from '../services/api'` to `import api from '../services/api'`
- Updated API call from `axios.get('/subjects')` to `api.get('/api/subjects')`

### ExamPage.js
- Changed `import axios from '../services/api'` to `import api from '../services/api'`
- Updated API call from `axios.get('/exams/history?page=0&size=10')` to `api.get('/api/exams/history?page=0&size=10')`

### WrongQuestionsPage.js
- Changed `import axios from '../services/api'` to `import api from '../services/api'`
- Updated API call from `axios.get('/wrong-questions')` to `api.get('/api/wrong-questions')`

## Verification Steps
1. **Backend API Test**: ✓ Confirmed subjects API returns data: `/api/subjects`
2. **Frontend Configuration**: ✓ Verified API service uses correct base URL
3. **Component Fixes**: ✓ Applied proper API instance and path prefixes
4. **Rebuild**: ✓ Rebuilt frontend with corrections
5. **Deployment**: ✓ Deployed updated frontend

## Components Affected
- **PracticePage.js**: Fixed subject listing functionality
- **ExamPage.js**: Fixed exam history retrieval
- **WrongQuestionsPage.js**: Fixed wrong questions display

## Status
✅ **RESOLVED**: Subject data now displays correctly in frontend
✅ **API Integration**: Proper backend communication restored
✅ **Frontend Components**: All pages now use correct API configuration
✅ **User Experience**: Subjects properly displayed in practice section

## Testing Performed
- Verified backend returns subject data via direct API call
- Confirmed frontend components use proper API configuration
- Tested rebuilt application functionality
- Validated user interface displays subjects correctly

The subject data display issue has been successfully resolved and subjects now appear correctly in the practice section.