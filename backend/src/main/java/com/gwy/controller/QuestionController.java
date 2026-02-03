package com.gwy.controller;

import com.gwy.dto.ApiResponse;
import com.gwy.model.Question;
import com.gwy.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) String difficulty) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questions;
        
        if (subjectId != null) {
            questions = questionService.getQuestionsBySubjectId(subjectId, pageable);
        } else {
            // 如果没有指定科目，可以默认获取某个科目或者全部
            questions = questionService.getQuestionsBySubjectId(1L, pageable);
        }
        
        // Note: 过滤难度级别需要在service层实现，这里简化处理
        return ResponseEntity.ok(ApiResponse.success("获取题目成功", questions));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Question>> getQuestion(@PathVariable Long id) {
        return questionService.findById(id)
                .map(question -> ResponseEntity.ok(ApiResponse.success("获取题目成功", question)))
                .orElse(ResponseEntity.ok(ApiResponse.error("题目不存在")));
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<ApiResponse<Object>> getQuestionsBySubject(
            @PathVariable Long subjectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questions = questionService.getQuestionsBySubjectId(subjectId, pageable);
        return ResponseEntity.ok(ApiResponse.success("获取题目成功", questions));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Object>> getQuestionsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questions = questionService.getQuestionsByCategoryId(categoryId, pageable);
        return ResponseEntity.ok(ApiResponse.success("获取题目成功", questions));
    }

    @GetMapping("/parent/{parentId}/children")
    public ResponseEntity<ApiResponse<Object>> getChildrenByParentId(
            @PathVariable Long parentId) {
        List<Question> childQuestions = questionService.getChildrenByParentId(parentId);
        return ResponseEntity.ok(ApiResponse.success("获取子题成功", childQuestions));
    }
}