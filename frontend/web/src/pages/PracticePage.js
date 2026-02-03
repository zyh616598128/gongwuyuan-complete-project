import React, { useState, useEffect } from 'react';
import { 
  Box, 
  Typography, 
  Card, 
  CardContent, 
  Button, 
  Grid, 
  Chip,
  Alert,
  CircularProgress
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import axios from '../services/api';

function PracticePage() {
  const [subjects, setSubjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchSubjects = async () => {
      try {
        const response = await axios.get('/subjects');
        setSubjects(response.data.data || []);
        setLoading(false);
      } catch (err) {
        setError('获取科目列表失败');
        setLoading(false);
      }
    };

    fetchSubjects();
  }, []);

  const handleSubjectSelect = (subjectId) => {
    navigate(`/practice/subject/${subjectId}`);
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
        题库练习
      </Typography>
      
      {error && <Alert severity="error">{error}</Alert>}
      
      <Typography variant="h6" sx={{ mt: 2, mb: 3 }}>
        请选择要练习的科目
      </Typography>
      
      <Grid container spacing={3}>
        {subjects.map((subject) => (
          <Grid item xs={12} sm={6} md={4} key={subject.id}>
            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  {subject.name}
                </Typography>
                <Typography variant="body2" color="textSecondary" paragraph>
                  {subject.description}
                </Typography>
                <Chip 
                  label={`题目数量: ${Math.floor(Math.random() * 1000) + 50}`} 
                  size="small" 
                  sx={{ mr: 1, mb: 1 }} 
                />
                <Chip 
                  label="最新" 
                  size="small" 
                  color="primary" 
                  sx={{ mr: 1, mb: 1 }} 
                />
                <Box sx={{ mt: 2 }}>
                  <Button 
                    variant="contained" 
                    fullWidth
                    onClick={() => handleSubjectSelect(subject.id)}
                  >
                    开始练习
                  </Button>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
        
        {subjects.length === 0 && (
          <Grid item xs={12}>
            <Alert severity="info">
              暂无可用科目，请联系管理员添加科目信息
            </Alert>
          </Grid>
        )}
      </Grid>
    </Box>
  );
}

export default PracticePage;