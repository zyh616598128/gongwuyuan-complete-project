import React, { useState, useEffect } from 'react';
import { 
  Box, 
  Typography, 
  Card, 
  CardContent, 
  Button, 
  Grid, 
  Alert,
  CircularProgress,
  Chip
} from '@mui/material';
import api from '../services/api';

function ExamPage() {
  const [exams, setExams] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchExams = async () => {
      try {
        // 获取考试历史记录
        const response = await api.get('/api/exams/history?page=0&size=10');
        setExams(response.data.data.content || []);
        setLoading(false);
      } catch (err) {
        setError('获取考试记录失败');
        setLoading(false);
      }
    };

    fetchExams();
  }, []);

  const handleStartExam = () => {
    // 这里应该打开考试对话框或跳转到考试页面
    alert('创建新考试功能待实现');
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '200px' }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ flexGrow: 1, p: 3 }}>
      <Typography variant="h4" gutterBottom>
        模拟考试
      </Typography>
      
      {error && <Alert severity="error">{error}</Alert>}
      
      <Grid container spacing={3}>
        <Grid item xs={12} md={8}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                历史考试记录
              </Typography>
              
              {exams.length > 0 ? (
                <Box>
                  {exams.map((exam, index) => (
                    <Box key={index} sx={{ borderBottom: '1px solid #eee', pb: 2, mb: 2 }}>
                      <Typography variant="subtitle1">{exam.title || `考试 ${index + 1}`}</Typography>
                      <Box sx={{ display: 'flex', gap: 2, mt: 1 }}>
                        <Chip 
                          label={`得分: ${exam.score || 0}`} 
                          size="small" 
                          color="primary" 
                        />
                        <Chip 
                          label={`正确率: ${(exam.accuracyRate || 0).toFixed(1)}%`} 
                          size="small" 
                          color="secondary" 
                        />
                        <Chip 
                          label={`用时: ${exam.duration || 0}分钟`} 
                          size="small" 
                          variant="outlined" 
                        />
                      </Box>
                    </Box>
                  ))}
                </Box>
              ) : (
                <Alert severity="info">
                  暂无考试记录
                </Alert>
              )}
            </CardContent>
          </Card>
        </Grid>
        
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                开始新考试
              </Typography>
              
              <Button 
                variant="contained" 
                color="primary" 
                fullWidth 
                sx={{ mb: 2 }}
                onClick={handleStartExam}
              >
                创建模拟考试
              </Button>
              
              <Typography variant="body2" color="textSecondary">
                选择科目和题目数量创建新的模拟考试
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
}

export default ExamPage;