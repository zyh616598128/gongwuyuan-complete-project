# Gongwuyuan (公务员) Civil Service Exam Preparation App

## Project Overview

This is a comprehensive civil service exam preparation application developed as part of the IntelliDev AI project. The application consists of both a backend server and a frontend Android application.

## Project Structure

### Backend
- **Framework**: Spring Boot
- **Language**: Java
- **Database**: MySQL
- **Cache**: Redis
- **Port**: 8080
- **Features**:
  - User authentication and management
  - Question bank with categories
  - Practice exam functionality
  - Progress tracking
  - Admin panel

### Frontend
- **Platform**: Android
- **Language**: Java
- **Architecture**: MVVM
- **Features**:
  - Login/Register functionality
  - Home screen with quick access
  - Question bank browsing
  - Practice exams
  - Exam simulations
  - Profile management

## Current Status

- ✅ Backend server fully implemented with user management, question bank, and exam features
- ✅ Android frontend with complete UI/UX implementation
- ✅ API integration ready between frontend and backend
- ✅ Cloud deployment completed on server 81.70.234.241
- 🔄 Android build process requires environment setup (in progress)

## Deployment

The backend is deployed and accessible at: http://81.70.234.241:8080

## Technologies Used

- **Backend**: Spring Boot, Java, MySQL, Redis
- **Frontend**: Android SDK, Java
- **Development**: IntelliJ IDEA, Android Studio
- **Deployment**: Docker, Cloud server

## API Endpoints

The backend provides the following main endpoints:
- `/api/auth/login` - User authentication
- `/api/auth/register` - User registration
- `/api/questions` - Access to question bank
- `/api/exams` - Exam management
- `/api/users/profile` - User profile management

## Project Files

The project contains:
- Complete backend implementation in `backend/`
- Complete Android frontend in `frontend/`
- Configuration files for deployment
- Documentation and design plans

## Next Steps

1. Complete Android build process
2. Package and deploy Android APK
3. Final testing of integrated system
4. Documentation completion

## Team

- **CEO**: zhuyinhang
- **CTO/Technical Lead**: OpenClaw AI Assistant
- **Development**: Automated AI-assisted development system

---
*Project generated and maintained by IntelliDev AI system*