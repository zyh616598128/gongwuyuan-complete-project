# Gongwuyuan Frontend Setup Guide

## Recommended Approach

The frontend is a React application built with Create React App. To run it properly, follow these steps:

### Prerequisites
- Node.js 16.x or higher
- npm or yarn package manager

### Installation Steps

1. **Install Dependencies**
   ```bash
   cd frontend/web
   npm install
   ```

2. **Start Development Server**
   ```bash
   npm start
   ```
   The app will run on http://localhost:3000

3. **Build for Production**
   ```bash
   npm run build
   ```

### Alternative: Lightweight Express Server

If the React development server doesn't work properly, we've provided a lightweight Express server as a fallback:

```bash
# Install Express
npm install express

# Run the lightweight server
node server.js
```

This serves the React build files statically and provides the same functionality with better stability.

### Troubleshooting Common Issues

1. **Module Resolution Errors**:
   - Clear npm cache: `npm cache clean --force`
   - Delete node_modules and reinstall: `rm -rf node_modules && npm install`

2. **Port Already in Use**:
   - Kill processes on port 3000: `lsof -ti:3000 | xargs kill -9`
   - Or change port in package.json: `"start": "PORT=3001 react-scripts start"`

3. **Peer Dependency Warnings**:
   - These are usually safe to ignore
   - If causing issues: `npm install --legacy-peer-deps`

### API Connection

The frontend connects to the backend at:
- Local: http://localhost:8081
- Cloud: http://81.70.234.241:8080

The proxy is configured in package.json to forward API requests.

### Key Features

- User authentication (login/register)
- Dashboard with statistics
- Practice modes for questions
- Exam simulation
- Wrong question collection
- Profile management
- Admin import functionality
- Smart PDF import for questions
- Material analysis for complex questions