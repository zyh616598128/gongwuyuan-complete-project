import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { 
  Box, 
  Typography, 
  Grid, 
  Paper, 
  Card, 
  CardContent, 
  CircularProgress 
} from '@mui/material';
import SchoolIcon from '@mui/icons-material/School';
import BarChartIcon from '@mui/icons-material/BarChart';
import ChecklistIcon from '@mui/icons-material/Checklist';
import { fetchProfile } from '../store/authSlice';

function DashboardPage() {
  const dispatch = useDispatch();
  const { user, isLoading } = useSelector(state => state.auth);

  useEffect(() => {
    if (!user || !user.id) {
      dispatch(fetchProfile());
    }
  }, [dispatch, user]);

  // Default values if user data is not loaded
  const userData = user || {
    username: '加载中...',
    nickname: '加载中...',
    studyDays: 0,
    totalQuestions: 0,
    answeredQuestions: 0,
    accuracyRate: 0.0
  };

  return (
    <Box sx={{ flexGrow: 1, p: 3 }}>
      <Typography variant="h4" gutterBottom>
        欢迎回来，{userData.nickname || userData.username}！
      </Typography>
      
      {isLoading && !user ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
          <CircularProgress />
        </Box>
      ) : (
        <>
          <Grid container spacing={3} sx={{ mt: 2 }}>
            {/* 学习天数卡片 */}
            <Grid item xs={12} sm={6} md={3}>
              <Card>
                <CardContent>
                  <SchoolIcon sx={{ fontSize: 40, color: 'primary.main', mb: 1 }} />
                  <Typography variant="h6" color="textSecondary">学习天数</Typography>
                  <Typography variant="h4" color="primary">{userData.studyDays || 0} 天</Typography>
                </CardContent>
              </Card>
            </Grid>
            
            {/* 总题数卡片 */}
            <Grid item xs={12} sm={6} md={3}>
              <Card>
                <CardContent>
                  <BarChartIcon sx={{ fontSize: 40, color: 'secondary.main', mb: 1 }} />
                  <Typography variant="h6" color="textSecondary">总题数</Typography>
                  <Typography variant="h4" color="secondary">{userData.totalQuestions || 0}</Typography>
                </CardContent>
              </Card>
            </Grid>
            
            {/* 已答题数卡片 */}
            <Grid item xs={12} sm={6} md={3}>
              <Card>
                <CardContent>
                  <ChecklistIcon sx={{ fontSize: 40, color: 'success.main', mb: 1 }} />
                  <Typography variant="h6" color="textSecondary">已答题目</Typography>
                  <Typography variant="h4" color="success.main">{userData.answeredQuestions || 0}</Typography>
                </CardContent>
              </Card>
            </Grid>
            
            {/* 正确率卡片 */}
            <Grid item xs={12} sm={6} md={3}>
              <Card>
                <CardContent>
                  <Typography variant="h6" color="textSecondary">正确率</Typography>
                  <Typography variant="h4" color="info.main">{(userData.accuracyRate || 0).toFixed(1)}%</Typography>
                </CardContent>
              </Card>
            </Grid>
          </Grid>
          
          <Grid container spacing={3} sx={{ mt: 3 }}>
            <Grid item xs={12} md={8}>
              <Paper sx={{ p: 2, height: '100%' }}>
                <Typography variant="h6" gutterBottom>
                  学习概况
                </Typography>
                <Typography variant="body1">
                  您已经连续学习了 {userData.studyDays || 0} 天，完成了 {userData.answeredQuestions || 0} 道题目，
                  正确率为 {(userData.accuracyRate || 0).toFixed(1)}%。
                  继续努力，争取更好的成绩！
                </Typography>
              </Paper>
            </Grid>
            <Grid item xs={12} md={4}>
              <Paper sx={{ p: 2, height: '100%' }}>
                <Typography variant="h6" gutterBottom>
                  快捷入口
                </Typography>
                <Typography variant="body2" color="textSecondary">
                  • 题库练习<br />
                  • 模拟考试<br />
                  • 错题本<br />
                  • 学习计划
                </Typography>
              </Paper>
            </Grid>
          </Grid>
        </>
      )}
    </Box>
  );
}

export default DashboardPage;