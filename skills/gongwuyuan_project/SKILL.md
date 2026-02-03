# Gongwuyuan Project Skill

## Description
This skill provides structured project management for the Gongwuyuan (公务员考试备考平台) project. It ensures all sub-agents follow consistent practices when working on this project.

## Purpose
- Maintain project consistency across all sub-agents
- Provide standardized approaches for common tasks
- Ensure proper documentation and code quality
- Guide proper development workflow

## Usage Instructions

When working on the Gongwuyuan project, sub-agents must:

### 1. Project Context Setup
Always start by understanding the current project state:
- Review the requirements-document.md for feature specifications
- Check the api-contract.md for API interfaces
- Refer to web-technical-spec.md for implementation guidelines
- Follow development-guidelines.md for best practices

### 2. Development Workflow
1. **Before starting work**:
   - Check current branch status with `git status`
   - Pull latest changes with `git pull origin main`
   - Review the issue/task requirements

2. **During development**:
   - Follow the naming conventions in development-guidelines.md
   - Maintain code quality standards
   - Update documentation as needed
   - Test functionality before committing

3. **Before submitting**:
   - Run tests to ensure functionality works
   - Verify code follows style guidelines
   - Update relevant documentation
   - Create meaningful commit messages following conventional commits

### 3. Common Tasks

#### Backend Development
- Location: `/backend/`
- Framework: Spring Boot + Java
- Database: MySQL + JPA
- Authentication: JWT

#### Frontend Development
- Web: React.js + Redux (planned)
- Mobile: Android Native
- API communication: Follow api-contract.md

#### Documentation Updates
- Always update relevant docs when changing functionality
- Follow the structure in existing documentation
- Maintain consistency with existing style

### 4. Quality Assurance
- Code must pass all existing tests
- New functionality should include appropriate tests
- Follow security best practices
- Ensure API responses match contract specifications

### 5. Commit Messages
Use conventional commits format:
- `feat(scope): description` - New features
- `fix(scope): description` - Bug fixes
- `docs(scope): description` - Documentation updates
- `refactor(scope): description` - Code refactoring

### 6. Error Handling
- If encountering merge conflicts, resolve them carefully
- If breaking changes are needed, update documentation accordingly
- Always verify functionality after resolving conflicts

## Constraints
- Always refer to existing documentation before making decisions
- Maintain backward compatibility where possible
- Follow the project's branching strategy
- Ensure all changes are properly tested