import React, { useState } from 'react';
import { 
  Box, 
  Typography, 
  Card, 
  CardContent, 
  Button, 
  Alert,
  LinearProgress,
  FormControl,
  InputLabel,
  Select,
  MenuItem
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

function AdminImportPage() {
  const [file, setFile] = useState(null);
  const [subjectId, setSubjectId] = useState('');
  const [uploading, setUploading] = useState(false);
  const [progress, setProgress] = useState(0);
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
    setError(null);
    setResult(null);
  };

  const handleSubjectChange = (e) => {
    setSubjectId(e.target.value);
  };

  const handleUpload = async () => {
    if (!file) {
      setError('请选择要上传的CSV文件');
      return;
    }

    if (!subjectId) {
      setError('请选择科目');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);
    formData.append('subjectId', subjectId);

    setUploading(true);
    setProgress(0);
    setError(null);
    setResult(null);

    try {
      // 模拟上传进度
      const interval = setInterval(() => {
        setProgress(prev => {
          if (prev >= 90) {
            clearInterval(interval);
            return 90;
          }
          return prev + 10;
        });
      }, 200);

      const response = await api.post('/api/import/questions/csv', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        onUploadProgress: (progressEvent) => {
          const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total);
          setProgress(percentCompleted);
        }
      });

      clearInterval(interval);
      setProgress(100);

      if (response.data.success) {
        setResult(response.data.data);
      } else {
        setError(response.data.message || '导入失败');
      }
    } catch (err) {
      setError(err.response?.data?.message || '上传过程中发生错误');
    } finally {
      setUploading(false);
    }
  };

  // 示例科目列表
  const subjects = [
    { id: 1, name: '行政职业能力测验' },
    { id: 2, name: '申论' },
    { id: 3, name: '公共基础知识' },
    { id: 4, name: '面试技巧' }
  ];

  return (
    <Box sx={{ flexGrow: 1, p: 3 }}>
      <Typography variant="h4" gutterBottom>
        题库导入
      </Typography>
      
      <Card>
        <CardContent>
          <Typography variant="h6" gutterBottom>
            从CSV文件导入题目
          </Typography>
          
          <Typography variant="body2" color="textSecondary" sx={{ mb: 2 }}>
            请按照指定格式准备CSV文件，第一行为标题行，包含以下列：
          </Typography>
          
          <Typography variant="body2" color="textSecondary" sx={{ mb: 3 }}>
            <ul>
              <li>题目内容</li>
              <li>选项A</li>
              <li>选项B</li>
              <li>选项C</li>
              <li>选项D</li>
              <li>答案（如：A 或 A,B）</li>
              <li>难度（EASY, MEDIUM, HARD）</li>
            </ul>
          </Typography>
          
          <FormControl fullWidth sx={{ mb: 2 }}>
            <InputLabel>选择科目</InputLabel>
            <Select
              value={subjectId}
              label="选择科目"
              onChange={handleSubjectChange}
              disabled={uploading}
            >
              {subjects.map((subject) => (
                <MenuItem key={subject.id} value={subject.id}>
                  {subject.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          
          <Box sx={{ mb: 2 }}>
            <input
              accept=".csv"
              type="file"
              onChange={handleFileChange}
              disabled={uploading}
              style={{ display: 'none' }}
              id="upload-file"
            />
            <label htmlFor="upload-file">
              <Button
                variant="outlined"
                component="span"
                disabled={uploading}
                sx={{ mr: 2 }}
              >
                选择CSV文件
              </Button>
            </label>
            {file && (
              <Typography variant="body2" color="textSecondary" sx={{ display: 'inline-block', ml: 2 }}>
                已选择: {file.name} ({(file.size / 1024).toFixed(2)} KB)
              </Typography>
            )}
          </Box>
          
          <Button
            variant="contained"
            color="primary"
            onClick={handleUpload}
            disabled={!file || !subjectId || uploading}
            sx={{ mb: 2 }}
          >
            {uploading ? '上传中...' : '开始导入'}
          </Button>
          
          {uploading && (
            <Box sx={{ width: '100%', mb: 2 }}>
              <LinearProgress variant="determinate" value={progress} />
              <Typography variant="caption" align="right">
                {Math.round(progress)}%
              </Typography>
            </Box>
          )}
          
          {error && <Alert severity="error">{error}</Alert>}
          
          {result && (
            <Alert severity="success">
              <Typography variant="h6">导入完成!</Typography>
              <Typography>总共处理: {result.totalProcessed} 条</Typography>
              <Typography>成功导入: {result.importedCount} 条</Typography>
              <Typography>错误数量: {result.errorCount} 条</Typography>
              {result.errors && result.errors.length > 0 && (
                <Box sx={{ mt: 1 }}>
                  <Typography variant="subtitle2">错误详情:</Typography>
                  {result.errors.slice(0, 5).map((err, index) => (
                    <Typography key={index} variant="body2" color="error">
                      {err}
                    </Typography>
                  ))}
                  {result.errors.length > 5 && (
                    <Typography variant="body2" color="textSecondary">
                      还有 {result.errors.length - 5} 个错误...
                    </Typography>
                  )}
                </Box>
              )}
            </Alert>
          )}
        </CardContent>
      </Card>
    </Box>
  );
}

export default AdminImportPage;