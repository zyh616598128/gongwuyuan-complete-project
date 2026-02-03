import React, { useState } from 'react';
import { 
  Box, 
  Typography, 
  Card, 
  CardContent, 
  Button, 
  Alert,
  LinearProgress,
  FormControlLabel,
  Switch,
  Tabs,
  Tab,
  TextField,
  Paper
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

function SmartImportPage() {
  const [file, setFile] = useState(null);
  const [textContent, setTextContent] = useState('');
  const [subjectId, setSubjectId] = useState('1');
  const [autoParse, setAutoParse] = useState(true);
  const [hasAnalysis, setHasAnalysis] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [progress, setProgress] = useState(0);
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);
  const [activeTab, setActiveTab] = useState(0);
  const navigate = useNavigate();

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
    setError(null);
    setResult(null);
  };

  const handleTextChange = (e) => {
    setTextContent(e.target.value);
    setError(null);
    setResult(null);
  };

  const handleSubjectChange = (e) => {
    setSubjectId(e.target.value);
  };

  const handleFileUpload = async () => {
    if (!file) {
      setError('请选择要上传的PDF文件');
      return;
    }

    if (!subjectId) {
      setError('请选择科目');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);
    formData.append('subjectId', subjectId);
    formData.append('autoParse', autoParse);

    setUploading(true);
    setProgress(0);
    setError(null);
    setResult(null);

    try {
      const response = await api.post('/api/smart-import/questions/pdf', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        onUploadProgress: (progressEvent) => {
          const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total);
          setProgress(percentCompleted);
        }
      });

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

  const handleTextImport = async () => {
    if (!textContent.trim()) {
      setError('请输入题目内容');
      return;
    }

    if (!subjectId) {
      setError('请选择科目');
      return;
    }

    setUploading(true);
    setProgress(0);
    setError(null);
    setResult(null);

    try {
      const response = await api.post('/api/smart-import/questions/text', {
        content: textContent,
        subjectId: parseInt(subjectId),
        hasAnalysis: hasAnalysis
      });

      if (response.data.success) {
        setResult(response.data.data);
      } else {
        setError(response.data.message || '导入失败');
      }
    } catch (err) {
      setError(err.response?.data?.message || '导入过程中发生错误');
    } finally {
      setUploading(false);
    }
  };

  const handlePreview = async () => {
    if (!file) {
      setError('请选择要上传的PDF文件');
      return;
    }

    setUploading(true);
    setProgress(0);
    setError(null);
    setResult(null);

    try {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('subjectId', subjectId);

      const response = await api.post('/api/smart-import/questions/preview', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        onUploadProgress: (progressEvent) => {
          const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total);
          setProgress(percentCompleted);
        }
      });

      if (response.data.success) {
        setResult({
          ...response.data.data,
          preview: true
        });
      } else {
        setError(response.data.message || '预览失败');
      }
    } catch (err) {
      setError(err.response?.data?.message || '预览过程中发生错误');
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
        智能题库导入
      </Typography>
      
      <Paper sx={{ mb: 2 }}>
        <Tabs value={activeTab} onChange={(e, newValue) => setActiveTab(newValue)}>
          <Tab label="PDF导入" />
          <Tab label="文本粘贴" />
          <Tab label="导入预览" />
        </Tabs>
      </Paper>

      <Card>
        <CardContent>
          {activeTab === 0 && (
            <Box>
              <Typography variant="h6" gutterBottom>
                从PDF文档智能导入题目
              </Typography>
              
              <Typography variant="body2" color="textSecondary" sx={{ mb: 2 }}>
                支持从真实的考试试卷PDF文档中自动提取题目、选项和答案
              </Typography>
              
              <Box sx={{ mb: 2 }}>
                <input
                  accept=".pdf"
                  type="file"
                  onChange={handleFileChange}
                  disabled={uploading}
                  style={{ display: 'none' }}
                  id="upload-pdf"
                />
                <label htmlFor="upload-pdf">
                  <Button
                    variant="outlined"
                    component="span"
                    disabled={uploading}
                    sx={{ mr: 2 }}
                  >
                    选择PDF文件
                  </Button>
                </label>
                {file && (
                  <Typography variant="body2" color="textSecondary" sx={{ display: 'inline-block', ml: 2 }}>
                    已选择: {file.name} ({(file.size / 1024 / 1024).toFixed(2)} MB)
                  </Typography>
                )}
              </Box>
              
              <Box sx={{ mb: 2 }}>
                <select 
                  value={subjectId} 
                  onChange={handleSubjectChange}
                  disabled={uploading}
                  style={{ padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
                >
                  {subjects.map((subject) => (
                    <option key={subject.id} value={subject.id}>
                      {subject.name}
                    </option>
                  ))}
                </select>
              </Box>
              
              <Box sx={{ mb: 2 }}>
                <FormControlLabel
                  control={
                    <Switch
                      checked={autoParse}
                      onChange={(e) => setAutoParse(e.target.checked)}
                      disabled={uploading}
                    />
                  }
                  label="自动解析题目"
                />
              </Box>
              
              <Box sx={{ display: 'flex', gap: 2, mb: 2 }}>
                <Button
                  variant="contained"
                  color="primary"
                  onClick={handleFileUpload}
                  disabled={!file || !subjectId || uploading}
                >
                  {uploading ? '导入中...' : '开始导入'}
                </Button>
                
                <Button
                  variant="outlined"
                  onClick={handlePreview}
                  disabled={!file || uploading}
                >
                  {uploading ? '预览中...' : '预览解析结果'}
                </Button>
              </Box>
            </Box>
          )}

          {activeTab === 1 && (
            <Box>
              <Typography variant="h6" gutterBottom>
                从文本内容导入题目
              </Typography>
              
              <Typography variant="body2" color="textSecondary" sx={{ mb: 2 }}>
                直接粘贴题目文本内容，系统将自动识别题目结构
              </Typography>
              
              <Box sx={{ mb: 2 }}>
                <textarea
                  value={textContent}
                  onChange={handleTextChange}
                  placeholder={`请粘贴题目内容，例如：\n1. 中国共产党的宗旨是什么？\nA. 为人民服务\nB. 为国家服务\nC. 为社会服务\nD. 为人民利益服务\n答案：A`}
                  rows={10}
                  cols={80}
                  disabled={uploading}
                  style={{
                    width: '100%',
                    padding: '10px',
                    borderRadius: '4px',
                    border: '1px solid #ccc',
                    fontFamily: 'monospace',
                    resize: 'vertical'
                  }}
                />
              </Box>
              
              <Box sx={{ mb: 2 }}>
                <select 
                  value={subjectId} 
                  onChange={handleSubjectChange}
                  disabled={uploading}
                  style={{ padding: '8px', borderRadius: '4px', border: '1px solid #ccc', marginRight: '10px' }}
                >
                  {subjects.map((subject) => (
                    <option key={subject.id} value={subject.id}>
                      {subject.name}
                    </option>
                  ))}
                </select>
                
                <FormControlLabel
                  control={
                    <Switch
                      checked={hasAnalysis}
                      onChange={(e) => setHasAnalysis(e.target.checked)}
                      disabled={uploading}
                    />
                  }
                  label="包含解析内容"
                />
              </Box>
              
              <Button
                variant="contained"
                color="primary"
                onClick={handleTextImport}
                disabled={!textContent.trim() || !subjectId || uploading}
              >
                {uploading ? '导入中...' : '开始导入'}
              </Button>
            </Box>
          )}

          {activeTab === 2 && (
            <Box>
              <Typography variant="h6" gutterBottom>
                导入预览功能
              </Typography>
              
              <Typography variant="body1" color="textSecondary" paragraph>
                上传PDF文档后，可以预览系统识别的题目内容，确认无误后再正式导入。
              </Typography>
              
              <Typography variant="body2" color="textSecondary">
                此功能可以帮助您检查文档解析的准确性，确保题目内容被正确提取。
              </Typography>
            </Box>
          )}
          
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
            <Alert severity={result.preview ? "info" : "success"}>
              <Typography variant="h6">
                {result.preview ? '预览结果' : '导入完成!'}
              </Typography>
              <Typography>处理题目数量: {result.totalCount || result.importedCount || 0} 道</Typography>
              {result.subjectId && <Typography>科目ID: {result.subjectId}</Typography>}
              {result.autoParsed && <Typography>自动解析: 是</Typography>}
              {result.hasAnalysis && <Typography>包含解析: 是</Typography>}
              
              {result.previewQuestions && result.previewQuestions.length > 0 && (
                <Box sx={{ mt: 2 }}>
                  <Typography variant="subtitle2">预览题目:</Typography>
                  {result.previewQuestions.map((q, idx) => (
                    <Typography key={idx} variant="body2" color="textSecondary" sx={{ mt: 1 }}>
                      {q.content ? q.content.substring(0, 100) + '...' : '题目内容'}
                    </Typography>
                  ))}
                </Box>
              )}
            </Alert>
          )}
        </CardContent>
      </Card>
    </Box>
  );
}

export default SmartImportPage;