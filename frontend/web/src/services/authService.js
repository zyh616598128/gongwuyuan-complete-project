import api from './api';

export const authService = {
  // User registration
  register: async (userData) => {
    return await api.post('/api/auth/register', userData);
  },

  // User login
  login: async (credentials) => {
    return await api.post('/api/auth/login', credentials);
  },

  // User logout
  logout: async () => {
    return await api.post('/api/auth/logout');
  },

  // Get user profile
  getProfile: async () => {
    return await api.get('/api/users/profile');
  },

  // Update user profile
  updateProfile: async (profileData) => {
    return await api.put('/api/users/profile', profileData);
  }
};