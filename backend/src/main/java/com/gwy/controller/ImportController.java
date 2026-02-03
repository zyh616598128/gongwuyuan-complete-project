package com.gwy.controller;

import com.gwy.dto.ApiResponse;
import com.gwy.model.Question;
import com.gwy.service.QuestionService;
import com.gwy.util.CsvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/import")
@CrossOrigin(origins = "*")
public class ImportController {

    @Autowired
    private QuestionService questionService;

    /**
     * 从CSV文件批量导入题目
     */
    @PostMapping("/questions/csv")
    public ResponseEntity<ApiResponse<Object>> importQuestionsFromCsv(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "subjectId", required = false) Long subjectId) {
        
        if (file.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.error("上传的文件为空"));
        }

        if (!file.getContentType().equals("text/csv") && 
            !file.getOriginalFilename().toLowerCase().endsWith(".csv")) {
            return ResponseEntity.ok(ApiResponse.error("请上传CSV格式的文件"));
        }

        List<Question> questions = new ArrayList<>();
        int importedCount = 0;
        int errorCount = 0;
        List<String> errors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                
                // 跳过标题行
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // 解析CSV行
                String[] fields = parseCsvLine(line);
                if (fields.length < 5) {
                    errors.add("第" + lineNumber + "行: 数据格式不正确，至少需要5列（题目内容,选项A,选项B,选项C,选项D,答案）");
                    errorCount++;
                    continue;
                }

                try {
                    Question question = createQuestionFromCsv(fields, subjectId);
                    questions.add(question);
                    importedCount++;
                } catch (Exception e) {
                    errors.add("第" + lineNumber + "行: " + e.getMessage());
                    errorCount++;
                }
            }

            // 批量保存题目
            if (!questions.isEmpty()) {
                questionService.saveAll(questions);
            }

            // 准备响应数据
            var responseData = new java.util.HashMap<String, Object>();
            responseData.put("totalProcessed", importedCount + errorCount);
            responseData.put("importedCount", importedCount);
            responseData.put("errorCount", errorCount);
            responseData.put("errors", errors);

            return ResponseEntity.ok(ApiResponse.success("题目导入完成", responseData));

        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("导入过程中发生错误: " + e.getMessage()));
        }
    }

    /**
     * 解析CSV行
     */
    private String[] parseCsvLine(String line) {
        return CsvParser.parseLine(line);
    }

    /**
     * 从CSV字段创建题目对象
     */
    private Question createQuestionFromCsv(String[] fields, Long subjectId) {
        Question question = new Question();
        
        // 题目内容
        question.setContent(fields[0].trim());
        
        // 解析选项（假设有A-D四个选项）
        List<String> options = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            if (i < fields.length) {
                options.add(fields[i].trim());
            }
        }
        question.setOptions(options);
        
        // 答案
        if (fields.length > 5) {
            String answerStr = fields[5].trim().toUpperCase();
            List<String> answers = new ArrayList<>();
            
            // 处理多选题，例如"A,B,C"或"ABCD"
            if (answerStr.contains(",")) {
                for (String ans : answerStr.split(",")) {
                    answers.add(ans.trim());
                }
            } else {
                // 单选题或连写选项
                for (char c : answerStr.toCharArray()) {
                    if (c >= 'A' && c <= 'D') {
                        answers.add(String.valueOf(c));
                    }
                }
            }
            question.setAnswers(answers);
        }
        
        // 解析难度（如果有）
        if (fields.length > 6) {
            String difficultyStr = fields[6].trim().toUpperCase();
            try {
                question.setDifficulty(Question.DifficultyLevel.valueOf(difficultyStr));
            } catch (IllegalArgumentException e) {
                // 默认难度为MEDIUM
                question.setDifficulty(Question.DifficultyLevel.MEDIUM);
            }
        } else {
            question.setDifficulty(Question.DifficultyLevel.MEDIUM);
        }
        
        // 题目类型 - 默认为单选题
        question.setType(Question.QuestionType.SINGLE_CHOICE);
        
        // 设置科目
        if (subjectId != null) {
            // 注意：实际实现中需要通过subjectId查询数据库获取Subject对象
            // 这里只是示意，实际需要注入SubjectService
        }
        
        return question;
    }
}