# Gongwuyuan (公考王) - 公务员考试备考平台

## 项目简介

Gongwuyuan 是一个专业的公务员考试备考平台，旨在为公务员考试考生提供全面的备考资源和智能学习体验。平台包含丰富的题库、智能练习系统、模拟考试功能以及个性化的学习分析。

## 项目特点

- **全面题库覆盖**：涵盖行政职业能力测验、申论、公共基础知识等多个科目
- **智能练习系统**：根据用户水平智能推荐题目
- **模拟考试环境**：真实模拟考试场景，提升应试能力
- **学习数据分析**：详细的个人学习报告和进步轨迹
- **多端支持**：支持Web端和移动端使用

## 技术架构

### 后端技术栈
- **框架**: Spring Boot 2.7+
- **语言**: Java 11+
- **数据库**: MySQL 8.0
- **缓存**: Redis 6.0+
- **认证**: JWT Token
- **构建工具**: Maven

### 前端技术栈
- **Web端**: React.js + Redux + Material-UI (即将开发)
- **移动端**: Android Native
- **API通信**: Retrofit + OkHttp

## 项目结构

```
gongwuyuan-complete-project/
├── backend/                 # 后端项目
│   ├── src/main/java/com/gwy/  # Java源码
│   ├── src/main/resources/     # 配置文件
│   └── pom.xml              # Maven配置
├── frontend/                # 前端项目
│   ├── web/                 # Web前端 (开发中)
│   └── app/                 # Android原生应用
├── docs/                    # 项目文档
├── requirements-document.md # 需求文档
├── api-contract.md         # API契约文档
├── web-technical-spec.md   # Web端技术规范
└── development-guidelines.md # 开发协作指南
```

## 快速开始

### 后端启动

1. **环境要求**
   - JDK 11+
   - MySQL 8.0+
   - Redis 6.0+

2. **配置数据库**
   ```bash
   # 创建数据库
   CREATE DATABASE gwy_exam CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **启动后端服务**
   ```bash
   cd backend
   mvn spring-boot:run
   ```

### 前端启动

1. **Web端（待开发）**
   ```bash
   cd frontend/web
   npm install
   npm start
   ```

2. **Android端**
   - 使用Android Studio打开 `frontend/app` 目录
   - 同步Gradle项目
   - 运行应用

## API文档

详细API接口文档请参考：[API契约文档](./api-contract.md)

## 开发规范

开发协作规范请参考：[开发协作指南](./development-guidelines.md)

## 需求说明

详细需求说明请参考：[需求文档](./requirements-document.md)

## 技术规范

Web端技术实现规范请参考：[Web端技术规范](./web-technical-spec.md)

## 部署说明

### 生产环境部署

使用Docker进行容器化部署：

```bash
# 构建镜像
docker build -t gongwuyuan-backend ./backend

# 运行容器
docker run -d -p 8080:8080 --name gongwuyuan-app gongwuyuan-backend
```

## 项目状态

- ✅ 后端基础功能完成
- ✅ Android移动端基础功能完成
- ⏳ Web端开发中
- ⏳ 移动端功能完善中

## 贡献指南

欢迎参与项目开发：

1. Fork 本项目
2. 创建特性分支
3. 提交更改
4. 发起 Pull Request

## 许可证

本项目采用 MIT 许可证。

## 联系方式

如有任何问题，请通过以下方式联系：
- 项目 Issues: [GitHub Issues](https://github.com/your-repo/issues)
- 邮箱: [maintainer-email@example.com]