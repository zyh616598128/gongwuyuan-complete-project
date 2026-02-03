package com.gwy.controller;

import com.gwy.dto.ApiResponse;
import com.gwy.model.Question;
import com.gwy.model.Subject;
import com.gwy.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/composite-questions")
@CrossOrigin(origins = "*")
public class CompositeQuestionController {

    @Autowired
    private QuestionService questionService;

    /**
     * 创建组合题（父题+子题）
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createCompositeQuestion(@RequestBody CompositeQuestionRequest request) {
        try {
            // 创建父题（组合题）
            Question parentQuestion = new Question();
            parentQuestion.setContent(request.getContent());
            parentQuestion.setReferenceMaterial(request.getReferenceMaterial());
            parentQuestion.setType(Question.QuestionType.COMPOSITE_QUESTION);
            parentQuestion.setDifficulty(request.getDifficulty());
            // 需要通过subjectId获取Subject对象
            
            // 保存父题
            Question savedParent = questionService.save(parentQuestion);
            
            // 创建子题
            for (int i = 0; i < request.getChildQuestions().size(); i++) {
                Question childQuestion = request.getChildQuestions().get(i);
                childQuestion.setParentQuestion(savedParent);
                childQuestion.setSortIndex(i);
                // 子题继承父题的科目，需要通过subjectId获取Subject对象
                
                // 保存子题
                questionService.save(childQuestion);
            }
            
            return ResponseEntity.ok(ApiResponse.success("组合题创建成功", savedParent.getId()));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("创建组合题失败: " + e.getMessage()));
        }
    }

    /**
     * 获取组合题及其子题
     */
    @GetMapping("/{parentId}")
    public ResponseEntity<ApiResponse<Object>> getCompositeQuestion(@PathVariable Long parentId) {
        Optional<Question> parentOpt = questionService.findById(parentId);
        
        if (parentOpt.isPresent()) {
            Question parent = parentOpt.get();
            
            // 获取所有子题
            List<Question> childQuestions = questionService.getChildrenByParentId(parentId);
            
            java.util.HashMap<String, Object> response = new java.util.HashMap<>();
            response.put("parentQuestion", parent);
            response.put("childQuestions", childQuestions);
            
            return ResponseEntity.ok(ApiResponse.success("获取组合题成功", response));
        } else {
            return ResponseEntity.ok(ApiResponse.error("组合题不存在"));
        }
    }

    /**
     * 从JSON文件导入组合题
     */
    @PostMapping("/import/json")
    public ResponseEntity<ApiResponse<Object>> importCompositeQuestionsFromJson(
            @RequestParam("file") MultipartFile file) {
        
        if (file.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.error("上传的文件为空"));
        }

        if (!file.getOriginalFilename().toLowerCase().endsWith(".json")) {
            return ResponseEntity.ok(ApiResponse.error("请上传JSON格式的文件"));
        }

        try {
            // 解析JSON内容并创建组合题
            String jsonContent = new String(file.getBytes());
            // 这里需要实现JSON解析逻辑
            
            return ResponseEntity.ok(ApiResponse.success("组合题导入成功", null));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("导入过程中发生错误: " + e.getMessage()));
        }
    }
    
    /**
     * 处理资料分析题的请求对象
     */
    public static class CompositeQuestionRequest {
        private String content;
        private String referenceMaterial; // 参考资料
        private Question.DifficultyLevel difficulty;
        private Long subjectId; // 使用ID代替对象引用
        private List<Question> childQuestions; // 子题列表
        
        // getters and setters
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        
        public String getReferenceMaterial() { return referenceMaterial; }
        public void setReferenceMaterial(String referenceMaterial) { this.referenceMaterial = referenceMaterial; }
        
        public Question.DifficultyLevel getDifficulty() { return difficulty; }
        public void setDifficulty(Question.DifficultyLevel difficulty) { this.difficulty = difficulty; }
        
        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        
        public List<Question> getChildQuestions() { return childQuestions; }
        public void setChildQuestions(List<Question> childQuestions) { this.childQuestions = childQuestions; }
    }
}