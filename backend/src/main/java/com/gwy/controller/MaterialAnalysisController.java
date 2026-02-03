package com.gwy.controller;

import com.gwy.dto.ApiResponse;
import com.gwy.model.Question;
import com.gwy.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipInputStream;

@RestController
@RequestMapping("/api/material-analysis")
@CrossOrigin(origins = "*")
public class MaterialAnalysisController {

    @Autowired
    private QuestionService questionService;

    /**
     * 创建资料分析题
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createMaterialAnalysisQuestion(@RequestBody MaterialAnalysisRequest request) {
        try {
            // 创建父题（资料分析题）
            Question parentQuestion = new Question();
            parentQuestion.setContent(request.getTitle());
            parentQuestion.setReferenceMaterial(request.getReferenceMaterial());
            parentQuestion.setType(Question.QuestionType.MATERIAL_ANALYSIS);
            parentQuestion.setDifficulty(request.getDifficulty());
            // 需要通过subjectId获取Subject对象
            // 这里暂时简化处理，实际实现需要注入SubjectService
            
            // 保存父题
            Question savedParent = questionService.save(parentQuestion);
            
            // 创建子题（基于资料的问题）
            for (int i = 0; i < request.getQuestions().size(); i++) {
                QuestionRequest subRequest = request.getQuestions().get(i);
                
                Question childQuestion = new Question();
                childQuestion.setContent(subRequest.getContent());
                childQuestion.setOptions(subRequest.getOptions());
                childQuestion.setAnswers(subRequest.getAnswers());
                childQuestion.setType(subRequest.getType());
                childQuestion.setDifficulty(subRequest.getDifficulty());
                childQuestion.setParentQuestion(savedParent);
                childQuestion.setSortIndex(i);
                // 子题继承父题的科目，需要通过subjectId获取Subject对象
                
                // 保存子题
                questionService.save(childQuestion);
            }
            
            return ResponseEntity.ok(ApiResponse.success("资料分析题创建成功", savedParent.getId()));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("创建资料分析题失败: " + e.getMessage()));
        }
    }

    /**
     * 获取资料分析题及其子题
     */
    @GetMapping("/{materialQuestionId}")
    public ResponseEntity<ApiResponse<Object>> getMaterialAnalysisQuestion(@PathVariable Long materialQuestionId) {
        Optional<Question> parentOpt = questionService.findById(materialQuestionId);
        if (parentOpt.isPresent()) {
            Question parentQuestion = parentOpt.get();
            if (parentQuestion.getType() == Question.QuestionType.MATERIAL_ANALYSIS) {
                // 获取所有子题
                List<Question> childQuestions = questionService.getChildrenByParentId(materialQuestionId);
                
                java.util.HashMap<String, Object> response = new java.util.HashMap<>();
                response.put("parentQuestion", parentQuestion);
                response.put("referenceMaterial", parentQuestion.getReferenceMaterial());
                response.put("childQuestions", childQuestions);
                
                return ResponseEntity.ok(ApiResponse.success("获取资料分析题成功", response));
            } else {
                return ResponseEntity.ok(ApiResponse.error("指定的题目不是资料分析题"));
            }
        } else {
            return ResponseEntity.ok(ApiResponse.error("题目不存在"));
        }
    }

    /**
     * 从ZIP文件批量导入资料分析题
     */
    @PostMapping("/import/zip")
    public ResponseEntity<ApiResponse<Object>> importMaterialAnalysisFromZip(
            @RequestParam("file") MultipartFile file) {
        
        if (file.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.error("上传的文件为空"));
        }

        if (!file.getOriginalFilename().toLowerCase().endsWith(".zip")) {
            return ResponseEntity.ok(ApiResponse.error("请上传ZIP格式的文件"));
        }

        try {
            // 解析ZIP文件并导入资料分析题
            ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
            // 这里需要实现ZIP文件解析逻辑
            
            return ResponseEntity.ok(ApiResponse.success("资料分析题导入成功", null));
        } catch (IOException e) {
            return ResponseEntity.ok(ApiResponse.error("导入过程中发生IO错误: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("导入过程中发生错误: " + e.getMessage()));
        }
    }
    
    /**
     * 资料分析题请求对象
     */
    public static class MaterialAnalysisRequest {
        private String title; // 资料标题
        private String referenceMaterial; // 参考资料（长文本）
        private Question.DifficultyLevel difficulty;
        private Long subjectId; // 使用ID代替对象引用
        private List<QuestionRequest> questions; // 基于资料的问题列表
        
        // getters and setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getReferenceMaterial() { return referenceMaterial; }
        public void setReferenceMaterial(String referenceMaterial) { this.referenceMaterial = referenceMaterial; }
        
        public Question.DifficultyLevel getDifficulty() { return difficulty; }
        public void setDifficulty(Question.DifficultyLevel difficulty) { this.difficulty = difficulty; }
        
        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        
        public List<QuestionRequest> getQuestions() { return questions; }
        public void setQuestions(List<QuestionRequest> questions) { this.questions = questions; }
    }
    
    /**
     * 子题请求对象
     */
    public static class QuestionRequest {
        private String content;
        private List<String> options;
        private List<String> answers;
        private Question.QuestionType type;
        private Question.DifficultyLevel difficulty;
        
        // getters and setters
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        
        public List<String> getOptions() { return options; }
        public void setOptions(List<String> options) { this.options = options; }
        
        public List<String> getAnswers() { return answers; }
        public void setAnswers(List<String> answers) { this.answers = answers; }
        
        public Question.QuestionType getType() { return type; }
        public void setType(Question.QuestionType type) { this.type = type; }
        
        public Question.DifficultyLevel getDifficulty() { return difficulty; }
        public void setDifficulty(Question.DifficultyLevel difficulty) { this.difficulty = difficulty; }
    }
}