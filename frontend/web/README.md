# Gongwuyuan Web Frontend

## 项目概述
Gongwuyuan (公考王) 是一个专业的公务员考试备考平台，此为Web前端应用。

## 技术栈
- React.js 18.x
- Redux Toolkit
- React Router v6
- Material-UI
- Axios for API communication

## 功能特性
- 用户认证系统（登录/注册）
- 个人仪表盘
- 题库练习
- 模拟考试
- 错题本
- 学习统计分析

## 安装和运行

### 环境要求
- Node.js >= 16.x
- npm 或 yarn

### 安装步骤
```bash
# 安装依赖
npm install

# 启动开发服务器
npm start
```

### 环境变量
创建 `.env` 文件配置后端API地址：
```
REACT_APP_API_BASE_URL=http://localhost:8081
```

### 构建生产版本
```bash
npm run build
```

## 项目结构
```
src/
├── components/     # 可复用组件
├── pages/          # 页面组件
├── services/       # API服务
├── store/          # Redux状态管理
├── styles/         # 样式文件
├── utils/          # 工具函数
├── App.js          # 主应用组件
└── index.js        # 应用入口
```

## API集成
前端与后端API通过Axios进行通信，已实现：
- 自动添加JWT token到请求头
- 401错误自动重定向到登录页
- 统一的错误处理

## 开发规范
- 使用ESLint和Prettier保持代码风格一致
- 组件命名采用帕斯卡命名法
- 文件命名采用短横线分隔法
- 所有API调用都通过service层进行