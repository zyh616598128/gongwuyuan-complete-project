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
 * 智能导入控制器
 * 支持从真实试卷文档自动提取题目
 */
@RestController
@RequestMapping("/api/smart-import")
@CrossOrigin(origins = "*")
public class SmartImportController {

    @Autowired
    private QuestionParsingService questionParsingService;
    
    @Autowired
    private QuestionService questionService;

    /**
     * 从PDF文档智能导入题目
     */
    @PostMapping("/questions/pdf")
    public ResponseEntity<ApiResponse<Object>> importQuestionsFromPdf(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "subjectId", required = false) Long subjectId,
            @RequestParam(value = "autoParse", defaultValue = "true") boolean autoParse) {
        
        if (file.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.error("上传的文件为空"));
        }

        if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            return ResponseEntity.ok(ApiResponse.error("请上传PDF格式的文件"));
        }

        try {
            List<Question> questions;
            
            if (autoParse) {
                // 使用智能解析服务
                questions = questionParsingService.parsePdfQuestions(file, subjectId);
            } else {
                // 这里可以实现其他解析方式
                questions = questionParsingService.parsePdfQuestions(file, subjectId);
            }
            
            // 保存题目
            if (!questions.isEmpty()) {
                questionService.saveAll(questions);
            }
            
            // 准备响应数据
            java.util.HashMap<String, Object> responseData = new java.util.HashMap<>();
            responseData.put("importedCount", questions.size());
            responseData.put("autoParsed", autoParse);
            responseData.put("subjectId", subjectId);
            
            return ResponseEntity.ok(ApiResponse.success("题目智能导入完成", responseData));

        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("导入过程中发生错误: " + e.getMessage()));
        }
    }

    /**
     * 从文本内容导入题目
     */
    @PostMapping("/questions/text")
    public ResponseEntity<ApiResponse<Object>> importQuestionsFromText(
            @RequestParam("content") String content,
            @RequestParam(value = "subjectId", required = false) Long subjectId,
            @RequestParam(value = "hasAnalysis", defaultValue = "false") boolean hasAnalysis) {
        
        try {
            List<Question> questions;
            
            if (hasAnalysis) {
                questions = questionParsingService.parseQuestionsWithAnalysis(content, subjectId);
            } else {
                questions = questionParsingService.parseTextQuestions(content, subjectId);
            }
            
            // 保存题目
            if (!questions.isEmpty()) {
                questionService.saveAll(questions);
            }
            
            // 准备响应数据
            java.util.HashMap<String, Object> responseData = new java.util.HashMap<>();
            responseData.put("importedCount", questions.size());
            responseData.put("hasAnalysis", hasAnalysis);
            responseData.put("subjectId", subjectId);
            
            return ResponseEntity.ok(ApiResponse.success("题目导入完成", responseData));

        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("导入过程中发生错误: " + e.getMessage()));
        }
    }

    /**
     * 预览解析结果（不保存到数据库）
     */
    @PostMapping("/questions/preview")
    public ResponseEntity<ApiResponse<Object>> previewQuestionsFromPdf(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "subjectId", required = false) Long subjectId) {
        
        if (file.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.error("上传的文件为空"));
        }

        if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            return ResponseEntity.ok(ApiResponse.error("请上传PDF格式的文件"));
        }

        try {
            // 解析但不保存
            List<Question> questions = questionParsingService.parsePdfQuestions(file, subjectId);
            
            // 准备预览数据
            java.util.HashMap<String, Object> responseData = new java.util.HashMap<>();
            responseData.put("totalCount", questions.size());
            responseData.put("previewQuestions", questions.size() > 5 ? questions.subList(0, 5) : questions);
            responseData.put("subjectId", subjectId);
            
            return ResponseEntity.ok(ApiResponse.success("预览解析完成", responseData));

        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("预览过程中发生错误: " + e.getMessage()));
        }
    }
}