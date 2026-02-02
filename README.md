# Gongwuyuan (公务员备考平台)

现代化的公务员备考平台，包含完整的前后端解决方案。

## 项目结构

- `backend/` - Spring Boot 后端服务
- `frontend/` - Android 原生前端应用
- `vue-frontend/` - Vue3 网页前端应用

## 后端 (Spring Boot)

基于 Spring Boot 2.7.0 构建，提供完整的 REST API 服务。

### 技术栈
- Spring Boot 2.7.0
- MySQL + JPA/Hibernate
- Redis 缓存
- JWT 身份验证
- Spring Security

### 功能特性
- 用户认证与授权
- 题库管理系统
- 考试系统
- 学习分析功能

### 启动方式
```bash
cd backend
mvn spring-boot:run
```

## Android 前端

使用 Java 和 XML 构建的原生 Android 应用。

### 功能特性
- 用户登录/注册
- 题目练习
- 模拟考试
- 学习统计
- 个人中心

## Vue3 网页前端

现代响应式网页应用，使用 Vue3、TypeScript、Vue Router 和 Pinia 构建。

### 技术栈
- Vue 3 (Composition API)
- TypeScript
- Vue Router
- Pinia (状态管理)
- Vite (构建工具)

### 启动方式
```bash
cd vue-frontend
npm install
npm run dev
```

## 部署

### 本地开发
1. 启动后端服务（端口 8080）
2. 启动前端应用

### 生产部署
参考各个子项目的部署说明。

## API 文档

后端 API 文档请参考相应的控制器类。

## 许可证

MIT License