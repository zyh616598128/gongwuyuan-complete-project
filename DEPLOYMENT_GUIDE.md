# Gongwuyuan Application - Deployment Guide

## Overview
This guide provides instructions for deploying the Gongwuyuan (公务员考试) application in production environments.

## Architecture
The application consists of:
- **Backend**: Spring Boot application serving REST APIs
- **Frontend**: React application providing user interface
- **Database**: MySQL for persistent data storage
- **Cache**: Redis for performance optimization

## Prerequisites
- Java 8+ (for backend)
- Node.js 14+ (for frontend)
- MySQL 8.0+
- Redis 6.0+

## Backend Deployment

### 1. Clone Repository
```bash
git clone <repository-url>
cd gongwuyuan-app/backend
```

### 2. Build Application
```bash
mvn clean package -DskipTests
```

### 3. Configure Environment
Create `application-prod.yml` with production settings:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gongwuyuan_prod
    username: prod_user
    password: secure_password
  redis:
    host: localhost
    port: 6379
server:
  port: 8080
jwt:
  secret: production_secret_key_here
```

### 4. Run Application
```bash
java -jar -Dspring.profiles.active=prod target/gwy-backend-1.0.0.jar
```

## Frontend Deployment

### 1. Install Dependencies
```bash
cd gongwuyuan-app/frontend/web
npm install
```

### 2. Build for Production
```bash
npm run build
```

### 3. Serve Application
```bash
# Using serve
npx serve -s build

# Or using nginx (recommended for production)
# Configure nginx to serve the build directory
```

## Docker Deployment (Recommended)

### 1. Build Docker Images
```bash
# Build backend image
cd backend
docker build -t gongwuyuan-backend .

# Build frontend image  
cd ../frontend/web
docker build -t gongwuyuan-frontend .
```

### 2. Use Docker Compose
Create `docker-compose.yml`:
```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root_password
      MYSQL_DATABASE: gongwuyuan
      MYSQL_USER: gwy_user
      MYSQL_PASSWORD: gwy_password
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data

  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - DB_HOST=mysql
      - REDIS_HOST=redis
    depends_on:
      - mysql
      - redis

  frontend:
    build: ./frontend/web
    ports:
      - "80:80"
    depends_on:
      - backend

volumes:
  mysql_data:
  redis_data:
```

### 3. Start Services
```bash
docker-compose up -d
```

## Configuration

### Environment Variables
- `SPRING_PROFILES_ACTIVE`: Active Spring profile (local, dev, prod)
- `DB_HOST`: Database hostname
- `DB_PORT`: Database port
- `REDIS_HOST`: Redis hostname
- `REDIS_PORT`: Redis port
- `JWT_SECRET`: JWT signing secret
- `SERVER_PORT`: Application server port

### API Base URL
Frontend requires backend URL configuration:
- Development: `http://localhost:8081`
- Production: `https://api.yourdomain.com`

## Smart Import Feature

### File Upload Limits
Configure upload limits in application properties:
```yaml
spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 50MB
```

### PDF Processing
The smart import feature uses Apache PDFBox for PDF processing:
- Supports PDF files up to 50MB
- Automatic text extraction
- Question structure recognition
- Preview functionality

## Monitoring and Logging

### Backend Logs
Logs are written to `logs/` directory:
- `app.log`: Application logs
- `access.log`: HTTP access logs
- `error.log`: Error logs

### Health Checks
- `/actuator/health`: Health status
- `/actuator/info`: Application info
- `/actuator/metrics`: Performance metrics

## Backup and Recovery

### Database Backup
```bash
mysqldump -u username -p database_name > backup.sql
```

### File Backup
Backup uploaded files and configuration separately:
```bash
tar -czf files_backup.tar.gz uploads/ config/
```

## Performance Tuning

### JVM Settings
For production deployment:
```bash
java -Xms1g -Xmx2g -jar application.jar
```

### Database Connection Pool
Configure HikariCP settings:
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      idle-timeout: 300000
      max-lifetime: 600000
```

## Security Considerations

### JWT Configuration
- Use strong secret keys (256-bit minimum)
- Set appropriate expiration times
- Enable HTTPS in production

### Input Validation
- All user inputs are validated
- SQL injection protection via JPA
- XSS protection via framework defaults

## Troubleshooting

### Common Issues
1. **Connection Refused**: Check if dependent services are running
2. **Out of Memory**: Increase heap size with `-Xmx` flag
3. **Slow Performance**: Verify database indexes and Redis connection
4. **File Upload Failures**: Check file size limits and disk space

### Debugging
Enable debug logging by adding to application properties:
```yaml
logging:
  level:
    com.gwy: DEBUG
    org.springframework: INFO
```

## Scaling

### Horizontal Scaling
- Load balancer for multiple frontend instances
- Database read replicas for scaling reads
- Redis cluster for distributed caching
- Message queue for background processing

### Vertical Scaling
- Increase JVM heap size
- More CPU cores for processing
- Additional RAM for caching

## Rollback Procedure
1. Stop current application
2. Deploy previous version
3. Verify functionality
4. Update load balancer if applicable

## Version Management
- Use semantic versioning (MAJOR.MINOR.PATCH)
- Tag releases in Git repository
- Maintain changelog for each release
- Test upgrades in staging environment first

This deployment guide ensures smooth operation of the Gongwuyuan application in production environments while maintaining security, performance, and reliability standards.