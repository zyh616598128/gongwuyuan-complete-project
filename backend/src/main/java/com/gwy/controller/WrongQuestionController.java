package com.gwy.controller;

import com.gwy.dto.ApiResponse;
import com.gwy.model.Question;
import com.gwy.model.User;
import com.gwy.model.WrongQuestion;
import com.gwy.service.QuestionService;
import com.gwy.service.UserService;
import com.gwy.service.WrongQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wrong-questions")
@CrossOrigin(origins = "*")
public class WrongQuestionController {

    @Autowired
    private WrongQuestionService wrongQuestionService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    /**
     * 获取错题列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getWrongQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long subjectId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);
            
            if (user == null) {
                return ResponseEntity.ok(ApiResponse.error("用户不存在"));
            }

            Pageable pageable = PageRequest.of(page, size);
            Page<WrongQuestion> wrongQuestionsPage;

            if (subjectId != null) {
                // 按科目筛选
                wrongQuestionsPage = wrongQuestionService.findByUserIdAndSubjectId(user.getId(), subjectId, pageable);
            } else {
                // 获取所有错题
                wrongQuestionsPage = wrongQuestionService.findByUserId(user.getId(), pageable);
            }

            // 转换为API响应格式
            List<Map<String, Object>> wrongQuestionsData = wrongQuestionsPage.getContent().stream().map(wq -> {
                Map<String, Object> item = new HashMap<>();
                
                Map<String, Object> questionData = new HashMap<>();
                Question question = wq.getQuestion();
                questionData.put("id", question.getId());
                questionData.put("content", question.getContent());
                questionData.put("analysis", question.getAnalysis());
                questionData.put("difficulty", question.getDifficulty());
                questionData.put("subjectId", question.getSubject().getId());
                
                // 处理选项
                if (question.getOptions() != null) {
                    List<Map<String, Object>> options = question.getOptions().stream().map(opt -> {
                        Map<String, Object> option = new HashMap<>();
                        option.put("id", opt.hashCode()); // 实际应用中应有正确的选项ID
                        option.put("content", opt);
                        option.put("isSelected", false); // 错题页面通常不显示用户之前的选择
                        return option;
                    }).collect(Collectors.toList());
                    questionData.put("options", options);
                }
                
                // 设置正确答案
                if (question.getAnswers() != null) {
                    questionData.put("correctAnswer", question.getAnswers().get(0)); // 简化处理，只取第一个答案
                }
                
                item.put("id", wq.getId());
                item.put("question", questionData);
                item.put("selectedAnswer", wq.getUserAnswer()); // 使用正确的字段名
                item.put("createdAt", wq.getCreateTime()); // 使用正确的字段名
                
                return item;
            }).collect(Collectors.toList());

            // 构建分页响应
            Map<String, Object> paginatedData = new HashMap<>();
            paginatedData.put("content", wrongQuestionsData);
            paginatedData.put("totalElements", wrongQuestionsPage.getTotalElements());
            paginatedData.put("totalPages", wrongQuestionsPage.getTotalPages());
            paginatedData.put("size", size);
            paginatedData.put("number", page);

            return ResponseEntity.ok(ApiResponse.success("获取错题列表成功", paginatedData));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("获取错题列表失败: " + e.getMessage()));
        }
    }

    /**
     * 删除错题
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteWrongQuestion(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);
            
            if (user == null) {
                return ResponseEntity.ok(ApiResponse.error("用户不存在"));
            }

            // 检查错题是否属于当前用户
            WrongQuestion wrongQuestion = wrongQuestionService.findById(id).orElse(null);
            if (wrongQuestion == null) {
                return ResponseEntity.ok(ApiResponse.error("错题不存在"));
            }
            
            if (!wrongQuestion.getUser().getId().equals(user.getId())) {
                return ResponseEntity.ok(ApiResponse.error("无权删除该错题"));
            }

            // 删除错题
            wrongQuestionService.deleteById(id);

            return ResponseEntity.ok(ApiResponse.success("错题删除成功"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("删除错题失败: " + e.getMessage()));
        }
    }

    /**
     * 添加错题（当用户答题错误时调用）
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> addWrongQuestion(@RequestBody Map<String, Object> requestData) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);
            
            if (user == null) {
                return ResponseEntity.ok(ApiResponse.error("用户不存在"));
            }

            Long questionId = ((Integer) requestData.get("questionId")).longValue();
            String selectedAnswer = (String) requestData.get("selectedAnswer");

            // 获取题目
            Question question = questionService.findById(questionId).orElse(null);
            if (question == null) {
                return ResponseEntity.ok(ApiResponse.error("题目不存在"));
            }

            // 检查是否已经存在该错题
            if (wrongQuestionService.existsByUserIdAndQuestionId(user.getId(), questionId)) {
                return ResponseEntity.ok(ApiResponse.success("错题已存在"));
            }

            // 检查是否已存在相同的错题记录
            Optional<WrongQuestion> existingWrongQuestion = wrongQuestionService.findByUserIdAndQuestionId(user.getId(), questionId);
            WrongQuestion wrongQuestion;
            
            if (existingWrongQuestion.isPresent()) {
                // 如果已存在，则更新错误次数和时间
                wrongQuestion = existingWrongQuestion.get();
                wrongQuestion.setErrorCount(wrongQuestion.getErrorCount() != null ? wrongQuestion.getErrorCount() + 1 : 1);
                wrongQuestion.setLastErrorTime(LocalDateTime.now());
                wrongQuestion.setUserAnswer(selectedAnswer);
            } else {
                // 如果不存在，则创建新的错题记录
                wrongQuestion = new WrongQuestion();
                wrongQuestion.setUser(user);
                wrongQuestion.setQuestion(question);
                wrongQuestion.setUserAnswer(selectedAnswer);
                wrongQuestion.setErrorCount(1);
                wrongQuestion.setFirstErrorTime(LocalDateTime.now());
                wrongQuestion.setLastErrorTime(LocalDateTime.now());
            }
            wrongQuestion.setUpdateTime(LocalDateTime.now());

            // 保存错题
            WrongQuestion savedWrongQuestion = wrongQuestionService.save(wrongQuestion);

            return ResponseEntity.ok(ApiResponse.success("错题添加成功"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("添加错题失败: " + e.getMessage()));
        }
    }
}