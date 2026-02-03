# Gongwuyuan Web端技术规范文档

## 1. 技术栈选择

### 1.1 前端技术栈
- **框架**：React.js 或 Vue.js (推荐React.js)
- **状态管理**：Redux Toolkit 或 Zustand
- **UI组件库**：Material-UI 或 Ant Design
- **路由管理**：React Router v6
- **HTTP客户端**：Axios
- **样式处理**：CSS Modules 或 Styled Components
- **构建工具**：Webpack 或 Vite

### 1.2 开发工具
- **代码编辑器**：VS Code
- **版本控制**：Git
- **包管理器**：npm 或 yarn
- **代码规范**：ESLint + Prettier
- **TypeScript**：推荐使用以提高代码质量

## 2. 项目结构

```
gongwuyuan-web/
├── public/
│   ├── index.html
│   └── favicon.ico
├── src/
│   ├── components/           # 可复用组件
│   │   ├── common/          # 通用组件
│   │   ├── ui/              # UI组件
│   │   └── layout/          # 布局组件
│   ├── pages/               # 页面组件
│   │   ├── Auth/            # 认证相关页面
│   │   ├── Dashboard/       # 仪表盘页面
│   │   ├── Practice/        # 练习页面
│   │   ├── Exam/            # 考试页面
│   │   ├── QuestionBank/    # 题库页面
│   │   └── Profile/         # 个人中心页面
│   ├── services/            # API服务
│   │   ├── api.js           # API配置
│   │   ├── auth.js          # 认证服务
│   │   ├── questions.js     # 题目服务
│   │   └── user.js          # 用户服务
│   ├── store/               # 状态管理
│   │   ├── index.js         # Store配置
│   │   ├── authSlice.js     # 认证状态
│   │   ├── questionSlice.js # 题目状态
│   │   └── userSlice.js     # 用户状态
│   ├── hooks/               # 自定义Hooks
│   │   ├── useAuth.js       # 认证Hook
│   │   └── useApi.js        # API Hook
│   ├── utils/               # 工具函数
│   │   ├── constants.js     # 常量定义
│   │   ├── helpers.js       # 辅助函数
│   │   └── validators.js    # 验证函数
│   ├── styles/              # 样式文件
│   │   ├── globals.css      # 全局样式
│   │   └── theme.js         # 主题配置
│   ├── types/               # TypeScript类型定义
│   │   ├── index.ts         # 通用类型
│   │   └── api.ts           # API类型
│   ├── App.js               # 应用根组件
│   ├── index.js             # 入口文件
│   └── routes.js            # 路由配置
├── package.json
├── .gitignore
├── .eslintrc.js
├── .prettierrc
└── README.md
```

## 3. API集成规范

### 3.1 API配置
```javascript
// src/services/api.js
import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://81.70.234.241:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 请求拦截器 - 自动添加JWT token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('jwt_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 响应拦截器 - 处理认证过期
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // 清除本地token并重定向到登录页
      localStorage.removeItem('jwt_token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;
```

### 3.2 认证服务
```javascript
// src/services/auth.js
import api from './api';

export const authService = {
  // 用户注册
  register: async (userData) => {
    return await api.post('/api/auth/register', userData);
  },

  // 用户登录
  login: async (credentials) => {
    return await api.post('/api/auth/login', credentials);
  },

  // 用户登出
  logout: async () => {
    return await api.post('/api/auth/logout');
  },

  // 刷新用户信息
  getProfile: async () => {
    return await api.get('/api/users/profile');
  }
};
```

### 3.3 题目服务
```javascript
// src/services/questions.js
import api from './api';

export const questionService = {
  // 获取科目列表
  getSubjects: async () => {
    return await api.get('/api/subjects');
  },

  // 获取题目列表
  getQuestions: async (params) => {
    return await api.get('/api/questions', { params });
  },

  // 获取特定科目的题目
  getQuestionsBySubject: async (subjectId, params) => {
    return await api.get(`/api/questions/subject/${subjectId}`, { params });
  },

  // 获取题目详情
  getQuestionById: async (id) => {
    return await api.get(`/api/questions/${id}`);
  }
};
```

## 4. 状态管理设计

### 4.1 认证状态管理
```javascript
// src/store/authSlice.js
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { authService } from '../services/auth';

export const login = createAsyncThunk(
  'auth/login',
  async ({ credentials }, { rejectWithValue }) => {
    try {
      const response = await authService.login(credentials);
      const { data } = response;
      
      // 保存token到localStorage
      localStorage.setItem('jwt_token', data);
      
      return { token: data };
    } catch (error) {
      return rejectWithValue(error.response?.data?.message || '登录失败');
    }
  }
);

export const fetchProfile = createAsyncThunk(
  'auth/fetchProfile',
  async (_, { rejectWithValue }) => {
    try {
      const response = await authService.getProfile();
      return response.data.data;
    } catch (error) {
      return rejectWithValue(error.response?.data?.message || '获取用户信息失败');
    }
  }
);

const authSlice = createSlice({
  name: 'auth',
  initialState: {
    user: null,
    token: localStorage.getItem('jwt_token'),
    isLoading: false,
    error: null,
  },
  reducers: {
    logout: (state) => {
      state.user = null;
      state.token = null;
      state.error = null;
      localStorage.removeItem('jwt_token');
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(login.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(login.fulfilled, (state, action) => {
        state.isLoading = false;
        state.token = action.payload.token;
      })
      .addCase(login.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      })
      .addCase(fetchProfile.fulfilled, (state, action) => {
        state.user = action.payload;
      });
  },
});

export const { logout } = authSlice.actions;
export default authSlice.reducer;
```

## 5. 组件设计规范

### 5.1 通用组件规范
```javascript
// 示例：按钮组件
// src/components/ui/Button.jsx
import React from 'react';
import PropTypes from 'prop-types';
import './Button.css';

const Button = ({ 
  children, 
  variant = 'primary', 
  size = 'medium', 
  disabled = false, 
  onClick, 
  className = '' 
}) => {
  const baseClass = 'btn';
  const classes = `${baseClass} ${baseClass}--${variant} ${baseClass}--${size} ${className}`;

  return (
    <button 
      className={classes}
      onClick={onClick}
      disabled={disabled}
      type="button"
    >
      {children}
    </button>
  );
};

Button.propTypes = {
  children: PropTypes.node.isRequired,
  variant: PropTypes.oneOf(['primary', 'secondary', 'danger', 'outline']),
  size: PropTypes.oneOf(['small', 'medium', 'large']),
  disabled: PropTypes.bool,
  onClick: PropTypes.func,
  className: PropTypes.string,
};

export default Button;
```

### 5.2 页面组件规范
```javascript
// 示例：登录页面
// src/pages/Auth/LoginPage.jsx
import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { login } from '../../store/authSlice';
import { Card, Input, Button, Alert } from '../../components';

const LoginPage = () => {
  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });
  
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { isLoading, error } = useSelector(state => state.auth);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await dispatch(login({ credentials: formData })).unwrap();
      navigate('/dashboard');
    } catch (err) {
      // 错误已在slice中处理
    }
  };

  return (
    <div className="login-page">
      <Card title="用户登录" className="login-card">
        {error && <Alert type="error" message={error} />}
        
        <form onSubmit={handleSubmit}>
          <Input
            label="用户名"
            name="username"
            value={formData.username}
            onChange={handleChange}
            required
          />
          
          <Input
            label="密码"
            name="password"
            type="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
          
          <Button 
            type="submit" 
            variant="primary" 
            disabled={isLoading}
            fullWidth
          >
            {isLoading ? '登录中...' : '登录'}
          </Button>
        </form>
      </Card>
    </div>
  );
};

export default LoginPage;
```

## 6. 路由配置

```javascript
// src/routes.js
import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './hooks/useAuth';

// 页面组件
import LoginPage from './pages/Auth/LoginPage';
import RegisterPage from './pages/Auth/RegisterPage';
import DashboardPage from './pages/Dashboard/DashboardPage';
import QuestionBankPage from './pages/QuestionBank/QuestionBankPage';
import PracticePage from './pages/Practice/PracticePage';
import ExamPage from './pages/Exam/ExamPage';
import ProfilePage from './pages/Profile/ProfilePage';

// 路由守卫组件
const PrivateRoute = ({ children }) => {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? children : <Navigate to="/login" />;
};

const PublicRoute = ({ children }) => {
  const { isAuthenticated } = useAuth();
  return !isAuthenticated ? children : <Navigate to="/dashboard" />;
};

const AppRoutes = () => {
  return (
    <Routes>
      {/* 公开路由 */}
      <Route path="/login" element={
        <PublicRoute>
          <LoginPage />
        </PublicRoute>
      } />
      <Route path="/register" element={
        <PublicRoute>
          <RegisterPage />
        </PublicRoute>
      } />
      
      {/* 私有路由 */}
      <Route path="/dashboard" element={
        <PrivateRoute>
          <DashboardPage />
        </PrivateRoute>
      } />
      <Route path="/questions" element={
        <PrivateRoute>
          <QuestionBankPage />
        </PrivateRoute>
      } />
      <Route path="/practice" element={
        <PrivateRoute>
          <PracticePage />
        </PrivateRoute>
      } />
      <Route path="/exam" element={
        <PrivateRoute>
          <ExamPage />
        </PrivateRoute>
      } />
      <Route path="/profile" element={
        <PrivateRoute>
          <ProfilePage />
        </PrivateRoute>
      } />
      
      {/* 默认路由 */}
      <Route path="/" element={<Navigate to="/dashboard" />} />
      <Route path="*" element={<Navigate to="/dashboard" />} />
    </Routes>
  );
};

export default AppRoutes;
```

## 7. 响应式设计规范

### 7.1 断点定义
```css
/* src/styles/breakpoints.css */
:root {
  --mobile: 480px;
  --tablet: 768px;
  --desktop: 1024px;
  --wide: 1200px;
}

@media (max-width: 479px) {
  /* 手机端样式 */
}

@media (min-width: 480px) and (max-width: 767px) {
  /* 小平板样式 */
}

@media (min-width: 768px) and (max-width: 1023px) {
  /* 平板样式 */
}

@media (min-width: 1024px) {
  /* 桌面端样式 */
}
```

## 8. 性能优化策略

### 8.1 代码分割
```javascript
// 使用React.lazy进行代码分割
import { lazy, Suspense } from 'react';

const DashboardPage = lazy(() => import('./pages/Dashboard/DashboardPage'));
const QuestionBankPage = lazy(() => import('./pages/QuestionBank/QuestionBankPage'));

const App = () => (
  <Suspense fallback={<div>Loading...</div>}>
    <AppRoutes />
  </Suspense>
);
```

### 8.2 图片优化
- 使用WebP格式图片
- 实施懒加载
- 适当的图片压缩

## 9. 安全规范

### 9.1 输入验证
- 前端和后端双重验证
- 防止XSS攻击
- 验证所有用户输入

### 9.2 认证安全
- JWT token安全存储
- 适当的过期时间设置
- HTTPS传输

## 10. 测试策略

### 10.1 单元测试
- 使用Jest和React Testing Library
- 组件测试
- Hook测试
- 工具函数测试

### 10.2 集成测试
- API集成测试
- 端到端测试
- 使用Cypress进行E2E测试

## 11. 部署配置

### 11.1 构建配置
```json
// package.json
{
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview",
    "test": "jest",
    "lint": "eslint src --ext js,jsx",
    "format": "prettier --write src/**/*.{js,jsx}"
  }
}
```

### 11.2 环境变量
```
REACT_APP_API_BASE_URL=http://81.70.234.241:8080
REACT_APP_ENV=production
```

这个技术规范文档为Web端开发提供了完整的指导，确保开发团队能够按照统一的标准进行开发。