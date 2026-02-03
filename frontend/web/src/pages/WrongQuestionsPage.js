import React, { useState, useEffect } from 'react';
import { 
  Box, 
  Typography, 
  Card, 
  CardContent, 
  Button, 
  Alert,
  CircularProgress,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper
} from '@mui/material';
import api from '../services/api';

function WrongQuestionsPage() {
  const [wrongQuestions, setWrongQuestions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchWrongQuestions = async () => {
      try {
        const response = await api.get('/api/wrong-questions');
        setWrongQuestions(response.data.data || []);
        setLoading(false);
      } catch (err) {
        setError('获取错题记录失败');
        setLoading(false);
      }
    };

    fetchWrongQuestions();
  }, []);

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
        错题本
      </Typography>
      
      {error && <Alert severity="error">{error}</Alert>}
      
      {wrongQuestions.length > 0 ? (
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>题目ID</TableCell>
                <TableCell>题目内容</TableCell>
                <TableCell>我的答案</TableCell>
                <TableCell>正确答案</TableCell>
                <TableCell>备注</TableCell>
                <TableCell>操作</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {wrongQuestions.map((item, index) => (
                <TableRow key={index}>
                  <TableCell>{item.questionId || 'N/A'}</TableCell>
                  <TableCell>{item.questionContent || '题目内容暂未获取'}</TableCell>
                  <TableCell>{item.userAnswer || '未填写'}</TableCell>
                  <TableCell>{item.correctAnswer || '待确认'}</TableCell>
                  <TableCell>{item.notes || '无备注'}</TableCell>
                  <TableCell>
                    <Button size="small" variant="outlined" color="primary">
                      重新练习
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      ) : (
        <Card>
          <CardContent>
            <Alert severity="info">
              暂无错题记录。在练习过程中答错的题目将会自动加入错题本。
            </Alert>
          </CardContent>
        </Card>
      )}
    </Box>
  );
}

export default WrongQuestionsPage;