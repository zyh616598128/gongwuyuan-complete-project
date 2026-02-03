import React, { useState, useEffect } from 'react';
import { 
  Box, 
  Typography, 
  Card, 
  CardContent, 
  Button, 
  Alert,
  Grid,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Divider
} from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import api from '../services/api';

function MaterialAnalysisPage() {
  const [materials, setMaterials] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchMaterialAnalysisQuestions();
  }, []);

  const fetchMaterialAnalysisQuestions = async () => {
    try {
      // 获取所有资料分析题（作为父题）
      const response = await api.get('/api/questions?subjectId=1&type=MATERIAL_ANALYSIS');
      setMaterials(response.data.data.content || []);
      setLoading(false);
    } catch (err) {
      setError('获取资料分析题失败');
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '200px' }}>
        <Typography>加载中...</Typography>
      </Box>
    );
  }

  return (
    <Box sx={{ flexGrow: 1, p: 3 }}>
      <Typography variant="h4" gutterBottom>
        资料分析题
      </Typography>
      
      {error && <Alert severity="error">{error}</Alert>}
      
      <Typography variant="h6" sx={{ mt: 2, mb: 3 }}>
        资料分析练习
      </Typography>
      
      {materials.length > 0 ? (
        materials.map((material) => (
          <Accordion key={material.id} sx={{ mb: 2 }}>
            <AccordionSummary expandIcon={<ExpandMoreIcon />}>
              <Typography variant="h6">{material.content}</Typography>
            </AccordionSummary>
            <AccordionDetails>
              <Card sx={{ mb: 2 }}>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    资料内容
                  </Typography>
                  <Typography variant="body1" color="textSecondary" paragraph>
                    {material.referenceMaterial}
                  </Typography>
                </CardContent>
              </Card>
              
              <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
                相关问题
              </Typography>
              
              <Divider sx={{ my: 2 }} />
              
              {material.subQuestions && material.subQuestions.map((question, index) => (
                <Card key={question.id} sx={{ mb: 2, p: 2 }}>
                  <Typography variant="subtitle1" sx={{ fontWeight: 'bold' }}>
                    第{index + 1}题: {question.content}
                  </Typography>
                  
                  {question.options && question.options.map((option, optIndex) => (
                    <Typography key={optIndex} variant="body2" sx={{ ml: 2 }}>
                      {String.fromCharCode(65 + optIndex)}. {option}
                    </Typography>
                  ))}
                  
                  <Box sx={{ mt: 1 }}>
                    <Button variant="outlined" size="small">
                      作答
                    </Button>
                  </Box>
                </Card>
              ))}
            </AccordionDetails>
          </Accordion>
        ))
      ) : (
        <Card>
          <CardContent>
            <Alert severity="info">
              暂无资料分析题，请联系管理员添加
            </Alert>
          </CardContent>
        </Card>
      )}
    </Box>
  );
}

export default MaterialAnalysisPage;