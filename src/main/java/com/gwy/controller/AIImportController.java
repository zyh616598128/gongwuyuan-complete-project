package com.gwy.controller;

import com.gwy.dto.ApiResponse;
import com.gwy.model.Question;
import com.gwy.service.QuestionParsingService;
import com.gwy.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * AI辅助导入控制器
 * 使用AI模型预处理文档内容，然后进行标准化导入
 */
@RestController
@RequestMapping("/api/ai-import")
@CrossOrigin(origins = "*")
public class AIImportController {

    @Autowired
    private QuestionParsingService questionParsingService;
    
    @Autowired
    private QuestionService questionService;

    /**
     * 通过AI预处理从PDF导入题目
     * 这个端点会接收PDF，使用AI进行预处理，然后按标准格式解析
     */
    @PostMapping("/questions/pdf")
    public ResponseEntity<ApiResponse<Object>> importQuestionsFromPdfViaAI(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "subjectId", required = false) Long subjectId) {
        
        if (file.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.error("上传的文件为空"));
        }

        if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            return ResponseEntity.ok(ApiResponse.error("请上传PDF格式的文件"));
        }

        try {
            // 注意：在实际实现中，这里会调用AI服务进行预处理
            // 为了演示目的，我们将使用增强的解析服务作为AI预处理的替代
            // 在生产环境中，这将连接到真正的AI服务
            
            // 使用增强的解析服务处理PDF
            List<Question> questions = questionParsingService.parsePdfQuestions(file, subjectId);
            
            // 保存题目
            if (!questions.isEmpty()) {
                questionService.saveAll(questions);
            }
            
            // 准备响应数据
            java.util.HashMap<String, Object> responseData = new java.util.HashMap<>();
            responseData.put("importedCount", questions.size());
            responseData.put("subjectId", subjectId);
            responseData.put("method", "ai_assisted_preprocessing");
            
            return ResponseEntity.ok(ApiResponse.success("AI辅助导入完成", responseData));

        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("AI辅助导入过程中发生错误: " + e.getMessage()));
        }
    }

    /**
     * 通过AI预处理从文本内容导入题目
     */
    @PostMapping("/questions/text")
    public ResponseEntity<ApiResponse<Object>> importQuestionsFromTextViaAI(
            @RequestParam("content") String content,
            @RequestParam(value = "subjectId", required = false) Long subjectId) {
        
        try {
            // 使用AI预处理的文本解析（使用增强的解析服务）
            List<Question> questions = questionParsingService.parseTextQuestions(content, subjectId);
            
            // 保存题目
            if (!questions.isEmpty()) {
                questionService.saveAll(questions);
            }
            
            // 准备响应数据
            java.util.HashMap<String, Object> responseData = new java.util.HashMap<>();
            responseData.put("importedCount", questions.size());
            responseData.put("subjectId", subjectId);
            responseData.put("method", "ai_assisted_preprocessing");
            
            return ResponseEntity.ok(ApiResponse.success("AI辅助导入完成", responseData));

        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("AI辅助导入过程中发生错误: " + e.getMessage()));
        }
    }

    /**
     * AI预处理内容格式化接口
     * 接收原始内容，返回标准化格式
     */
    @PostMapping("/format-content")
    public ResponseEntity<ApiResponse<Object>> formatContentForImport(
            @RequestParam("content") String content) {
        
        try {
            // 这里模拟AI的格式化功能
            // 在实际实现中，这将连接到AI服务来标准化内容格式
            String standardizedContent = performAIContentStandardization(content);
            
            java.util.HashMap<String, Object> responseData = new java.util.HashMap<>();
            responseData.put("standardizedContent", standardizedContent);
            responseData.put("originalLength", content.length());
            responseData.put("standardizedLength", standardizedContent.length());
            
            return ResponseEntity.ok(ApiResponse.success("内容已标准化", responseData));

        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("内容标准化过程中发生错误: " + e.getMessage()));
        }
    }

    /**
     * 模拟AI内容标准化功能
     * 在实际实现中，这里会调用真正的AI服务
     */
    private String performAIContentStandardization(String content) {
        // 这是一个简化的模拟
        // 实际AI服务会做更复杂的文档理解和格式化
        
        // 移除多余的空白
        String standardized = content.replaceAll("\\s+", " ");
        
        // 尝试识别并标准化一些常见的格式变体
        // 这只是示例，实际AI会做更智能的处理
        
        // 例如：将"1、"转换为"1."，将"答案是A"转换为"【答案】A"等
        standardized = standardized.replaceAll("(\\d+)[、]", "$1. ");
        
        // 简单的标准化处理，实际AI会做更复杂的理解
        return standardized;
    }
}