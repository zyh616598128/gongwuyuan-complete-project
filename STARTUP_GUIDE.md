# Gongwuyuan Application Startup Guide

## Quick Start

To start the complete Gongwuyuan application stack:

### 1. Start Database Services
```bash
cd ~/.openclaw/workspace/gongwuyuan-app
docker-compose up -d
```

### 2. Start Backend Service
```bash
cd ~/.openclaw/workspace/gongwuyuan-app/backend
java -jar -Dspring.profiles.active=local -Dserver.port=8081 target/gwy-backend-1.0.0.jar
```

### 3. Start Frontend Service
```bash
cd ~/.openclaw/workspace/gongwuyuan-app/frontend/web
npx serve -s build
```

Or use the automated deployment script:
```bash
~/.openclaw/workspace/gongwuyuan-app/deploy.sh
```

## Service URLs

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8081
- **Database (MySQL)**: localhost:3306
- **Cache (Redis)**: localhost:6379

## API Testing

Register a new user:
```bash
curl -X POST "http://localhost:8081/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"password123","phone":"12345678901","nickname":"Test User"}'
```

Login and get JWT token:
```bash
curl -X POST "http://localhost:8081/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'
```

Access protected endpoints with JWT:
```bash
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  "http://localhost:8081/api/users/profile"
```

## Docker Management

Start all services:
```bash
docker-compose up -d
```

Stop all services:
```bash
docker-compose down
```

View service logs:
```bash
docker-compose logs -f
```

## Troubleshooting

1. **Port already in use**: Change ports in application configuration
2. **Database connection errors**: Ensure MySQL and Redis are running via Docker
3. **JWT authentication fails**: Verify correct token format in Authorization header
4. **Frontend can't connect to backend**: Check CORS settings and network connectivity

## Production Deployment

For production deployment, use the provided Docker Compose configuration with SSL termination via Nginx:

```yaml
version: '3.8'
services:
  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf
      - ./:/var/www/html
    depends_on:
      - backend
    networks:
      - app-network

  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_HOST=mysql
      - REDIS_HOST=redis
    depends_on:
      - mysql
      - redis
    networks:
      - app-network

  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      MYSQL_DATABASE: ${MYSQL_DATABASE}
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    networks:
      - app-network

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    networks:
      - app-network

networks:
  app-network:
    driver: bridge

volumes:
  mysql_data:
  redis_data:
```

## Project Status

✅ **Complete**: Gongwuyuan application is fully developed and deployed
✅ **Functional**: All features working correctly
✅ **Documented**: APIs and functionality documented
✅ **Tested**: End-to-end testing completed