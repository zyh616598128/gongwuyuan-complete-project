package com.gwy.controller;

import com.gwy.dto.ApiResponse;
import com.gwy.dto.CreateExamRequest;
import com.gwy.dto.SubmitExamRequest;
import com.gwy.model.ExamPaper;
import com.gwy.model.ExamRecord;
import com.gwy.model.Question;
import com.gwy.model.Subject;
import com.gwy.model.User;
import com.gwy.service.ExamService;
import com.gwy.service.QuestionService;
import com.gwy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin(origins = "*")
public class ExamController {

    @Autowired
    private ExamService examService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    /**
     * 创建模拟考试
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createExam(@Valid @RequestBody CreateExamRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);
            
            if (user == null) {
                return ResponseEntity.ok(ApiResponse.error("用户不存在"));
            }

            // 获取请求参数
            Long subjectId = request.getSubjectId();
            String title = request.getTitle();
            Integer durationMinutes = request.getDurationMinutes() != null ? request.getDurationMinutes() : 120;
            Integer questionCount = request.getQuestionCount() != null ? request.getQuestionCount() : 100;

            // 获取科目
            Subject subject = new Subject();
            subject.setId(subjectId);
            
            // 创建考试试卷
            ExamPaper examPaper = new ExamPaper();
            examPaper.setTitle(title);
            examPaper.setSubject(subject);
            examPaper.setTotalTime(durationMinutes);
            examPaper.setTotalQuestions(questionCount);
            examPaper.setCreateTime(LocalDateTime.now());
            examPaper.setUpdateTime(LocalDateTime.now());
            examPaper.setIsActive(true);

            // TODO: 实际从数据库中获取指定科目的题目
            // 这里只是示例，实际需要从数据库获取随机题目
            List<Question> questions = questionService.getQuestionsBySubjectId(subjectId, PageRequest.of(0, questionCount)).getContent();
            examPaper.setQuestions(questions);

            // 保存考试试卷
            ExamPaper savedExamPaper = examService.saveExamPaper(examPaper);

            // 准备返回数据
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("id", savedExamPaper.getId());
            responseData.put("title", savedExamPaper.getTitle());
            responseData.put("subjectId", savedExamPaper.getSubject() != null ? savedExamPaper.getSubject().getId() : subjectId);
            responseData.put("durationMinutes", durationMinutes);
            responseData.put("startTime", LocalDateTime.now());
            responseData.put("questions", savedExamPaper.getQuestions());

            return ResponseEntity.ok(ApiResponse.success("考试创建成功", responseData));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("创建考试失败: " + e.getMessage()));
        }
    }

    /**
     * 提交考试答案
     */
    @PostMapping("/{examId}/submit")
    public ResponseEntity<ApiResponse<Object>> submitExam(@PathVariable Long examId, @Valid @RequestBody SubmitExamRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);
            
            if (user == null) {
                return ResponseEntity.ok(ApiResponse.error("用户不存在"));
            }

            // 获取用户提交的答案
            List<SubmitExamRequest.AnswerDto> answers = request.getAnswers();

            // 获取考试试卷
            Optional<ExamPaper> examPaperOpt = examService.findExamPaperById(examId);
            if (examPaperOpt.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.error("考试不存在"));
            }
            ExamPaper examPaper = examPaperOpt.get();

            // 计算分数 - 这里需要更精确的实现
            int correctAnswers = 0;
            int totalQuestions = examPaper.getQuestions().size();
            
            // 实际的评分逻辑需要根据题目正确答案和用户答案对比
            // 由于现有模型结构，我们简化处理
            for (int i = 0; i < Math.min(answers.size(), examPaper.getQuestions().size()); i++) {
                // 这里需要具体的评分逻辑，根据实际情况实现
                // 暂时假设每个答案都是正确的（实际应根据正确答案判断）
            }

            // 创建考试记录
            ExamRecord examRecord = new ExamRecord();
            examRecord.setUser(user);
            examRecord.setExamPaper(examPaper);
            examRecord.setStartTime(LocalDateTime.now().minusMinutes(examPaper.getTotalTime() != null ? examPaper.getTotalTime() : 120)); // 简化处理
            examRecord.setEndTime(LocalDateTime.now());
            examRecord.setDuration((long) (examPaper.getTotalTime() != null ? examPaper.getTotalTime() : 120) * 60); // 转换为秒
            examRecord.setAnswers(answers != null ? answers.stream().map(a -> a.getSelectedOptionId()).toList() : null);
            examRecord.setCreateTime(LocalDateTime.now());
            examRecord.setUpdateTime(LocalDateTime.now());
            
            // 设置总分和获得分数（简化计算）
            examRecord.setTotalScore(examPaper.getTotalScore() != null ? examPaper.getTotalScore() : totalQuestions * 100);
            examRecord.setObtainedScore(examPaper.getTotalScore() != null ? 
                (int) (examPaper.getTotalScore() * ((double) correctAnswers / totalQuestions)) : 
                correctAnswers * 100);
            
            // 保存考试记录
            ExamRecord savedRecord = examService.saveExamRecord(examRecord);

            // 返回结果
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("examId", examId);
            responseData.put("score", examRecord.getObtainedScore());
            responseData.put("correctAnswers", correctAnswers);
            responseData.put("totalQuestions", totalQuestions);
            responseData.put("accuracyRate", totalQuestions > 0 ? (double) correctAnswers / totalQuestions * 100 : 0.0);
            responseData.put("duration", examRecord.getDuration() != null ? examRecord.getDuration() / 60 : 0); // 转换为分钟

            return ResponseEntity.ok(ApiResponse.success("考试提交成功", responseData));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("提交考试失败: " + e.getMessage()));
        }
    }

    /**
     * 获取考试历史
     */
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<Object>> getExamHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);
            
            if (user == null) {
                return ResponseEntity.ok(ApiResponse.error("用户不存在"));
            }

            Pageable pageable = PageRequest.of(page, size);
            List<ExamRecord> examRecords = examService.getExamRecordsByUserId(user.getId());

            // 简化处理，不分页
            List<Map<String, Object>> historyData = examRecords.stream().map(record -> {
                Map<String, Object> item = new HashMap<>();
                item.put("id", record.getId());
                item.put("title", record.getExamPaper() != null ? record.getExamPaper().getTitle() : "未知试卷");
                item.put("subjectName", record.getExamPaper() != null && record.getExamPaper().getSubject() != null 
                    ? record.getExamPaper().getSubject().getName() : "未知科目"); // 需要确保关联数据加载
                item.put("score", record.getObtainedScore());
                item.put("totalQuestions", record.getExamPaper() != null ? record.getExamPaper().getTotalQuestions() : 0);
                item.put("accuracyRate", record.getObtainedScore() != null && record.getTotalScore() != null && record.getTotalScore() > 0 
                    ? (double) record.getObtainedScore() / record.getTotalScore() * 100 : 0.0);
                item.put("duration", record.getDuration() != null ? record.getDuration() / 60 : 0); // 转换为分钟
                item.put("examDate", record.getCreateTime());
                return item;
            }).collect(Collectors.toList());

            // 简化的分页对象
            Map<String, Object> paginatedData = new HashMap<>();
            paginatedData.put("content", historyData);
            paginatedData.put("totalElements", historyData.size());
            paginatedData.put("totalPages", 1);
            paginatedData.put("size", size);
            paginatedData.put("number", page);

            return ResponseEntity.ok(ApiResponse.success("获取考试历史成功", paginatedData));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("获取考试历史失败: " + e.getMessage()));
        }
    }
}