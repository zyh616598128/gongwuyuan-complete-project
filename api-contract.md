# Gongwuyuan API 接口契约文档

## 1. API 基础信息

### 1.1 基础URL
- 生产环境: `http://81.70.234.241:8080`
- 本地开发: `http://localhost:8081`

### 1.2 认证方式
- JWT Token 认证
- 请求头格式: `Authorization: Bearer <token>`

### 1.3 数据格式
- 请求/响应格式: JSON
- 字符编码: UTF-8
- 日期格式: ISO 8601 (YYYY-MM-DDTHH:mm:ss.SSS)

### 1.4 响应格式
```json
{
  "success": true,
  "message": "操作成功",
  "data": {},
  "timestamp": "2024-01-01T10:00:00.000"
}
```

## 2. 用户认证接口

### 2.1 用户注册
- **接口路径**: `POST /api/auth/register`
- **接口描述**: 新用户注册
- **是否需要认证**: 否

#### 请求参数
```json
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123",
  "phone": "13800138000",
  "nickname": "测试用户"
}
```

#### 请求参数说明
| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| username | string | 是 | 用户名，长度3-20字符 |
| email | string | 是 | 邮箱地址 |
| password | string | 是 | 密码，长度6-20字符 |
| phone | string | 是 | 手机号码 |
| nickname | string | 是 | 昵称 |

#### 响应示例
```json
{
  "success": true,
  "message": "注册成功",
  "data": null,
  "timestamp": "2024-01-01T10:00:00.000"
}
```

#### 错误响应
- 400: 参数错误
- 409: 用户名或邮箱已存在

### 2.2 用户登录
- **接口路径**: `POST /api/auth/login`
- **接口描述**: 用户登录
- **是否需要认证**: 否

#### 请求参数
```json
{
  "username": "testuser",
  "password": "password123"
}
```

#### 响应示例
```json
{
  "success": true,
  "message": "登录成功",
  "data": "eyJhbGciOiJIUzUxMiJ9...",
  "timestamp": "2024-01-01T10:00:00.000"
}
```

#### 响应说明
- `data`: JWT Token字符串

#### 错误响应
- 400: 用户名或密码错误
- 401: 账户被禁用

### 2.3 获取用户资料
- **接口路径**: `GET /api/users/profile`
- **接口描述**: 获取当前登录用户资料
- **是否需要认证**: 是

#### 响应示例
```json
{
  "success": true,
  "message": "获取用户资料成功",
  "data": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    "nickname": "测试用户",
    "phone": "13800138000",
    "createTime": "2024-01-01T10:00:00.000",
    "studyDays": 5,
    "totalQuestions": 100,
    "answeredQuestions": 80,
    "accuracyRate": 85.0
  },
  "timestamp": "2024-01-01T10:00:00.000"
}
```

#### 用户资料对象说明
| 属性 | 类型 | 描述 |
|------|------|------|
| id | integer | 用户ID |
| username | string | 用户名 |
| email | string | 邮箱 |
| nickname | string | 昵称 |
| phone | string | 手机号 |
| createTime | string | 注册时间 |
| studyDays | integer | 学习天数 |
| totalQuestions | integer | 总题数 |
| answeredQuestions | integer | 已答题目数 |
| accuracyRate | number | 正确率百分比 |

## 3. 题目接口

### 3.1 获取科目列表
- **接口路径**: `GET /api/subjects`
- **接口描述**: 获取所有考试科目
- **是否需要认证**: 是

#### 查询参数
无

#### 响应示例
```json
{
  "success": true,
  "message": "获取科目列表成功",
  "data": [
    {
      "id": 1,
      "name": "行政职业能力测验",
      "description": "公务员考试科目 - 行政职业能力测验",
      "isActive": true,
      "createTime": "2024-01-01T10:00:00.000",
      "updateTime": "2024-01-01T10:00:00.000"
    }
  ],
  "timestamp": "2024-01-01T10:00:00.000"
}
```

### 3.2 获取题目列表
- **接口路径**: `GET /api/questions`
- **接口描述**: 分页获取题目列表
- **是否需要认证**: 是

#### 查询参数
| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| page | integer | 否 | 页码，默认0 |
| size | integer | 否 | 每页大小，默认10 |
| subjectId | integer | 否 | 科目ID筛选 |
| difficulty | string | 否 | 难度级别筛选 |

#### 响应示例
```json
{
  "success": true,
  "message": "获取题目成功",
  "data": {
    "content": [
      {
        "id": 1,
        "content": "题目内容...",
        "options": [
          {"id": 1, "content": "选项A"},
          {"id": 2, "content": "选项B"},
          {"id": 3, "content": "选项C"},
          {"id": 4, "content": "选项D"}
        ],
        "correctAnswer": 1,
        "explanation": "题目解析...",
        "difficulty": "MEDIUM",
        "subjectId": 1,
        "createTime": "2024-01-01T10:00:00.000"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10,
      "sort": {}
    },
    "totalElements": 100,
    "totalPages": 10,
    "size": 10,
    "number": 0
  },
  "timestamp": "2024-01-01T10:00:00.000"
}
```

### 3.3 获取题目详情
- **接口路径**: `GET /api/questions/{id}`
- **接口描述**: 获取指定题目详情
- **是否需要认证**: 是

#### 路径参数
| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| id | integer | 是 | 题目ID |

#### 响应示例
```json
{
  "success": true,
  "message": "获取题目详情成功",
  "data": {
    "id": 1,
    "content": "题目内容...",
    "options": [
      {"id": 1, "content": "选项A", "isSelected": false},
      {"id": 2, "content": "选项B", "isSelected": false},
      {"id": 3, "content": "选项C", "isSelected": false},
      {"id": 4, "content": "选项D", "isSelected": false}
    ],
    "correctAnswer": 1,
    "explanation": "题目解析...",
    "difficulty": "MEDIUM",
    "subjectId": 1,
    "createTime": "2024-01-01T10:00:00.000"
  },
  "timestamp": "2024-01-01T10:00:00.000"
}
```

### 3.4 按科目获取题目
- **接口路径**: `GET /api/questions/subject/{subjectId}`
- **接口描述**: 按科目获取题目列表
- **是否需要认证**: 是

#### 路径参数
| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| subjectId | integer | 是 | 科目ID |

#### 查询参数
| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| page | integer | 否 | 页码，默认0 |
| size | integer | 否 | 每页大小，默认10 |

## 4. 考试接口

### 4.1 创建模拟考试
- **接口路径**: `POST /api/exams`
- **接口描述**: 创建新的模拟考试
- **是否需要认证**: 是

#### 请求参数
```json
{
  "subjectId": 1,
  "title": "模拟考试标题",
  "durationMinutes": 120,
  "questionCount": 100
}
```

#### 请求参数说明
| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| subjectId | integer | 是 | 考试科目ID |
| title | string | 是 | 考试标题 |
| durationMinutes | integer | 是 | 考试时长（分钟） |
| questionCount | integer | 是 | 题目数量 |

#### 响应示例
```json
{
  "success": true,
  "message": "考试创建成功",
  "data": {
    "id": 1,
    "title": "模拟考试标题",
    "subjectId": 1,
    "durationMinutes": 120,
    "startTime": "2024-01-01T10:00:00.000",
    "questions": [
      {
        "id": 1,
        "content": "题目内容...",
        "options": [...]
      }
    ]
  },
  "timestamp": "2024-01-01T10:00:00.000"
}
```

### 4.2 提交考试答案
- **接口路径**: `POST /api/exams/{examId}/submit`
- **接口描述**: 提交考试答案
- **是否需要认证**: 是

#### 路径参数
| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| examId | integer | 是 | 考试ID |

#### 请求参数
```json
{
  "answers": [
    {
      "questionId": 1,
      "selectedOptionId": 1
    }
  ]
}
```

#### 响应示例
```json
{
  "success": true,
  "message": "考试提交成功",
  "data": {
    "examId": 1,
    "score": 85,
    "correctAnswers": 85,
    "totalQuestions": 100,
    "accuracyRate": 85.0,
    "duration": 110
  },
  "timestamp": "2024-01-01T10:00:00.000"
}
```

### 4.3 获取考试历史
- **接口路径**: `GET /api/exams/history`
- **接口描述**: 获取用户考试历史
- **是否需要认证**: 是

#### 查询参数
| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| page | integer | 否 | 页码，默认0 |
| size | integer | 否 | 每页大小，默认10 |

#### 响应示例
```json
{
  "success": true,
  "message": "获取考试历史成功",
  "data": {
    "content": [
      {
        "id": 1,
        "title": "模拟考试标题",
        "subjectName": "行政职业能力测验",
        "score": 85,
        "totalQuestions": 100,
        "accuracyRate": 85.0,
        "duration": 110,
        "examDate": "2024-01-01T10:00:00.000"
      }
    ],
    "totalElements": 10,
    "totalPages": 1,
    "size": 10,
    "number": 0
  },
  "timestamp": "2024-01-01T10:00:00.000"
}
```

## 5. 错题接口

### 5.1 获取错题列表
- **接口路径**: `GET /api/wrong-questions`
- **接口描述**: 获取用户的错题列表
- **是否需要认证**: 是

#### 查询参数
| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| page | integer | 否 | 页码，默认0 |
| size | integer | 否 | 每页大小，默认10 |
| subjectId | integer | 否 | 科目ID筛选 |

#### 响应示例
```json
{
  "success": true,
  "message": "获取错题列表成功",
  "data": {
    "content": [
      {
        "id": 1,
        "question": {
          "id": 1,
          "content": "题目内容...",
          "options": [...],
          "correctAnswer": 1,
          "explanation": "题目解析..."
        },
        "selectedAnswer": 2,
        "createdAt": "2024-01-01T10:00:00.000"
      }
    ],
    "totalElements": 10,
    "totalPages": 1,
    "size": 10,
    "number": 0
  },
  "timestamp": "2024-01-01T10:00:00.000"
}
```

### 5.2 删除错题
- **接口路径**: `DELETE /api/wrong-questions/{id}`
- **接口描述**: 删除指定错题
- **是否需要认证**: 是

#### 路径参数
| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| id | integer | 是 | 错题记录ID |

#### 响应示例
```json
{
  "success": true,
  "message": "错题删除成功",
  "data": null,
  "timestamp": "2024-01-01T10:00:00.000"
}
```

## 6. 通用错误码

| 错误码 | HTTP状态码 | 描述 |
|--------|------------|------|
| 200 | 200 | 成功 |
| 400 | 400 | 请求参数错误 |
| 401 | 401 | 未授权，需要登录 |
| 403 | 403 | 禁止访问，权限不足 |
| 404 | 404 | 资源不存在 |
| 409 | 409 | 冲突，如用户名已存在 |
| 500 | 500 | 服务器内部错误 |

## 7. 数据类型定义

### 7.1 题目难度级别
- EASY: 简单
- MEDIUM: 中等
- HARD: 困难

### 7.2 科目对象
```json
{
  "id": 1,
  "name": "科目名称",
  "description": "科目描述",
  "isActive": true,
  "createTime": "2024-01-01T10:00:00.000",
  "updateTime": "2024-01-01T10:00:00.000"
}
```

### 7.3 选项对象
```json
{
  "id": 1,
  "content": "选项内容",
  "isSelected": false
}
```

这份API契约文档为前后端开发提供了明确的接口规范，确保双方能够按照统一的标准进行开发和集成。