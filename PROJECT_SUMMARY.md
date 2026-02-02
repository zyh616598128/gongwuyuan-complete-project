# 公务员备考App项目总结

## 项目概述
这是一个专为公务员考生设计的备考应用，采用前后端分离架构，后端使用Spring Boot框架，前端使用Android原生开发。

## 技术架构

### 后端（Spring Boot）
- **框架**: Spring Boot 2.7.0
- **数据库**: MySQL
- **缓存**: Redis
- **认证**: JWT
- **安全**: Spring Security
- **ORM**: JPA/Hibernate

### 前端（Android）
- **语言**: Java
- **最低SDK**: API 21
- **网络请求**: Retrofit + OkHttp
- **图片加载**: Glide
- **JSON解析**: Gson

## 项目结构

### 后端目录结构
```
backend/
├── src/main/java/com/gwy/
│   ├── controller/          # 控制器层
│   ├── service/             # 业务逻辑层
│   ├── repository/          # 数据访问层
│   ├── model/               # 实体类
│   ├── dto/                 # 数据传输对象
│   ├── config/              # 配置类
│   ├── exception/           # 异常处理
│   └── util/                # 工具类
└── src/main/resources/
    └── application.yml      # 配置文件
```

### 前端目录结构
```
frontend/
├── app/src/main/java/com/gwy/exam/
│   ├── model/               # 数据模型
│   ├── api/                 # 网络接口
│   ├── ui/                  # 界面组件
│   └── utils/               # 工具类
└── app/src/main/res/        # 资源文件
    ├── layout/              # 布局文件
    ├── values/              # 字符串、颜色、样式等
    └── drawable/            # 图片资源
```

## 核心功能模块

### 1. 用户管理
- 用户注册/登录
- 个人信息管理
- JWT身份验证

### 2. 题库系统
- 题目分类管理（按科目、章节）
- 支持多种题型（单选、多选、判断、填空、论述）
- 题目难度分级

### 3. 考试系统
- 模拟考试
- 自动阅卷评分
- 考试记录管理

### 4. 学习辅助
- 错题本功能
- 学习进度跟踪
- 个人统计分析

## 数据模型

### 主要实体关系
- **User** (用户) - 一对多 - **ExamRecord** (考试记录)
- **User** (用户) - 一对多 - **WrongQuestion** (错题记录)
- **Subject** (科目) - 一对多 - **Question** (题目)
- **Category** (分类) - 一对多 - **Question** (题目)
- **ExamPaper** (试卷) - 一对多 - **ExamRecord** (考试记录)
- **Question** (题目) - 多对多 - **ExamPaper** (试卷)

## API接口设计

### 认证相关
- POST `/api/auth/login` - 用户登录
- POST `/api/auth/register` - 用户注册

### 题目相关
- GET `/api/questions` - 获取题目列表
- GET `/api/questions/{id}` - 获取单个题目
- GET `/api/questions/subject/{subjectId}` - 按科目获取题目
- GET `/api/questions/category/{categoryId}` - 按分类获取题目

### 科目相关
- GET `/api/subjects` - 获取科目列表

### 考试相关
- GET `/api/exams` - 获取试卷列表
- POST `/api/exams/{id}/submit` - 提交考试结果

## 安全措施
- 使用JWT进行身份验证
- 密码使用BCrypt加密存储
- 输入参数校验
- SQL注入防护（JPA自动防护）

## 部署说明

### 后端部署
1. 安装JDK 8+
2. 配置MySQL数据库
3. 配置Redis缓存
4. 修改application.yml中的数据库连接信息
5. 运行 `mvn spring-boot:run`

### 前端部署
1. 使用Android Studio打开项目
2. 同步Gradle依赖
3. 连接设备或启动模拟器
4. 运行应用

## 开发建议
1. 持续集成/持续部署(CI/CD)流程
2. 单元测试和集成测试
3. 日志管理和监控
4. 性能优化和缓存策略
5. 定期备份数据