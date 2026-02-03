import React, { useState, useEffect } from 'react';
import { 
  Box, 
  Typography, 
  Card, 
  CardContent, 
  TextField, 
  Button, 
  Alert,
  Grid,
  CircularProgress
} from '@mui/material';
import { useDispatch, useSelector } from 'react-redux';
import { fetchProfile, updateUserProfile } from '../store/authSlice';

function ProfilePage() {
  const dispatch = useDispatch();
  const { user, isLoading, updateError } = useSelector(state => state.auth);
  const [formData, setFormData] = useState({
    phone: '',
    nickname: ''
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!user || !user.id) {
      dispatch(fetchProfile());
    } else {
      setFormData({
        phone: user.phone || '',
        nickname: user.nickname || ''
      });
    }
  }, [dispatch, user]);

  useEffect(() => {
    if (user) {
      setFormData({
        phone: user.phone || '',
        nickname: user.nickname || ''
      });
    }
  }, [user]);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    
    try {
      await dispatch(updateUserProfile(formData));
      // 更新成功后不需要额外操作，Redux store会被更新
    } catch (error) {
      console.error('更新用户资料失败:', error);
    } finally {
      setLoading(false);
    }
  };

  if (isLoading && !user) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '200px' }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ flexGrow: 1, p: 3 }}>
      <Typography variant="h4" gutterBottom>
        个人中心
      </Typography>
      
      <Grid container spacing={3}>
        <Grid item xs={12} md={8}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                个人资料
              </Typography>
              
              <form onSubmit={handleSubmit}>
                <Grid container spacing={2}>
                  <Grid item xs={12}>
                    <TextField
                      fullWidth
                      label="用户名"
                      name="username"
                      value={user?.username || ''}
                      disabled
                    />
                  </Grid>
                  
                  <Grid item xs={12}>
                    <TextField
                      fullWidth
                      label="邮箱"
                      name="email"
                      value={user?.email || ''}
                      disabled
                    />
                  </Grid>
                  
                  <Grid item xs={12} sm={6}>
                    <TextField
                      fullWidth
                      label="昵称"
                      name="nickname"
                      value={formData.nickname}
                      onChange={handleChange}
                      helperText="2-20个字符"
                    />
                  </Grid>
                  
                  <Grid item xs={12} sm={6}>
                    <TextField
                      fullWidth
                      label="手机号"
                      name="phone"
                      value={formData.phone}
                      onChange={handleChange}
                      helperText="请输入有效的手机号"
                    />
                  </Grid>
                  
                  <Grid item xs={12}>
                    <TextField
                      fullWidth
                      label="注册时间"
                      name="createTime"
                      value={user?.createTime ? new Date(user.createTime).toLocaleString('zh-CN') : ''}
                      disabled
                    />
                  </Grid>
                  
                  <Grid item xs={12}>
                    <Button
                      type="submit"
                      variant="contained"
                      color="primary"
                      disabled={loading}
                    >
                      {loading ? '更新中...' : '更新资料'}
                    </Button>
                  </Grid>
                </Grid>
                
                {updateError && (
                  <Alert severity="error" sx={{ mt: 2 }}>
                    更新失败: {updateError}
                  </Alert>
                )}
              </form>
            </CardContent>
          </Card>
        </Grid>
        
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                学习统计
              </Typography>
              
              <Typography variant="body2" sx={{ mb: 1 }}>
                学习天数: <strong>{user?.studyDays || 0} 天</strong>
              </Typography>
              <Typography variant="body2" sx={{ mb: 1 }}>
                总题数: <strong>{user?.totalQuestions || 0} 题</strong>
              </Typography>
              <Typography variant="body2" sx={{ mb: 1 }}>
                已答: <strong>{user?.answeredQuestions || 0} 题</strong>
              </Typography>
              <Typography variant="body2" sx={{ mb: 2 }}>
                正确率: <strong>{(user?.accuracyRate || 0).toFixed(1)}%</strong>
              </Typography>
              
              <Typography variant="caption" color="textSecondary">
                保持学习，不断提升！
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
}

export default ProfilePage;