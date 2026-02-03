import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { Container } from '@mui/material';
import { ThemeProvider, createTheme } from '@mui/material/styles';

import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DashboardPage from './pages/DashboardPage';
import PracticePage from './pages/PracticePage';
import ExamPage from './pages/ExamPage';
import WrongQuestionsPage from './pages/WrongQuestionsPage';
import ProfilePage from './pages/ProfilePage';
import Layout from './components/Layout';

const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#e57373',
    },
    background: {
      default: '#f5f5f5',
    },
  },
});

function App() {
  const { isAuthenticated } = useSelector(state => state.auth);

  return (
    <ThemeProvider theme={theme}>
      <Container maxWidth={false} disableGutters>
        <Routes>
          <Route path="/login" element={!isAuthenticated ? <LoginPage /> : <Navigate to="/dashboard" />} />
          <Route path="/register" element={!isAuthenticated ? <RegisterPage /> : <Navigate to="/dashboard" />} />
          <Route 
            path="/" 
            element={
              isAuthenticated ? 
                <Layout><DashboardPage /></Layout> : 
                <Navigate to="/login" />
            } 
          />
          <Route 
            path="/dashboard" 
            element={
              isAuthenticated ? 
                <Layout><DashboardPage /></Layout> : 
                <Navigate to="/login" />
            } 
          />
          <Route 
            path="/practice" 
            element={
              isAuthenticated ? 
                <Layout><PracticePage /></Layout> : 
                <Navigate to="/login" />
            } 
          />
          <Route 
            path="/exam" 
            element={
              isAuthenticated ? 
                <Layout><ExamPage /></Layout> : 
                <Navigate to="/login" />
            } 
          />
          <Route 
            path="/wrong-questions" 
            element={
              isAuthenticated ? 
                <Layout><WrongQuestionsPage /></Layout> : 
                <Navigate to="/login" />
            } 
          />
          <Route 
            path="/profile" 
            element={
              isAuthenticated ? 
                <Layout><ProfilePage /></Layout> : 
                <Navigate to="/login" />
            } 
          />
        </Routes>
      </Container>
    </ThemeProvider>
  );
}

export default App;