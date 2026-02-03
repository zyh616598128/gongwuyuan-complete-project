package com.gwy.service;

import com.gwy.model.Question;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 智能题目解析服务
 * 支持多种格式文档的题目自动提取
 */
@Service
public class QuestionParsingService {

    /**
     * 从PDF文件解析题目
     */
    public List<Question> parsePdfQuestions(MultipartFile file, Long subjectId) throws Exception {
        PDDocument document = PDDocument.load(file.getBytes());
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        document.close();
        
        return parseTextQuestions(text, subjectId);
    }

    /**
     * 通用文本解析方法
     */
    public List<Question> parseTextQuestions(String text, Long subjectId) {
        List<Question> questions = new ArrayList<>();
        
        // 移除多余的空白字符
        text = text.replaceAll("\\s+", " ");
        
        // 匹配题目模式：数字+. 题目内容 A.选项 B.选项 C.选项 D.选项
        Pattern questionPattern = Pattern.compile(
            "(\\d+[\\.、])\\s*(.*?)((?:[A-Z][\\.、][^A-Z]+)+)([A-Z])?\\s*(?:答案|解析|【答案】|【解析】)?\\s*([A-Z])?",
            Pattern.DOTALL
        );
        
        Matcher matcher = questionPattern.matcher(text);
        
        while (matcher.find()) {
            String fullMatch = matcher.group(0);
            String questionNum = matcher.group(1);
            String questionContent = matcher.group(2).trim();
            String optionsText = matcher.group(3).trim();
            String extractedAnswer = matcher.group(4); // 可能是从选项中推断的答案
            String explicitAnswer = matcher.group(5); // 明确标注的答案
            
            // 解析选项
            Map<String, String> optionsMap = parseOptions(optionsText);
            
            // 创建题目对象
            Question question = new Question();
            question.setContent(questionContent);
            question.setType(com.gwy.model.Question.QuestionType.SINGLE_CHOICE);
            question.setSubject(null); // 需要通过subjectId获取
            question.setOptions(new ArrayList<>(optionsMap.values()));
            
            // 确定答案（优先使用明确标注的答案）
            String finalAnswer = explicitAnswer != null ? explicitAnswer : extractedAnswer;
            if (finalAnswer != null) {
                List<String> answers = new ArrayList<>();
                answers.add(finalAnswer);
                question.setAnswers(answers);
            }
            
            questions.add(question);
        }
        
        return questions;
    }

    /**
     * 解析选项文本
     */
    private Map<String, String> parseOptions(String optionsText) {
        Map<String, String> options = new LinkedHashMap<>();
        
        // 匹配 A.选项内容 B.选项内容 C.选项内容 D.选项内容
        Pattern optionPattern = Pattern.compile("([A-Z])[\\.、]([^A-Z]+?)(?=[A-Z][\\.、]|$)");
        Matcher matcher = optionPattern.matcher(optionsText);
        
        while (matcher.find()) {
            String optionLetter = matcher.group(1);
            String optionText = matcher.group(2).trim();
            options.put(optionLetter, optionText);
        }
        
        return options;
    }

    /**
     * 解析包含解析的题目（常见于真题试卷）
     */
    public List<Question> parseQuestionsWithAnalysis(String text, Long subjectId) {
        List<Question> questions = new ArrayList<>();
        
        // 匹配题目+解析模式
        Pattern pattern = Pattern.compile(
            "(\\d+[\\.、])\\s*(.*?)(?=\\d+[\\.、]|$)",
            Pattern.DOTALL
        );
        
        Matcher matcher = pattern.matcher(text);
        
        while (matcher.find()) {
            String questionBlock = matcher.group(0);
            
            // 解析单个题目块
            Question question = parseSingleQuestionBlock(questionBlock, subjectId);
            if (question != null) {
                questions.add(question);
            }
        }
        
        return questions;
    }

    /**
     * 解析单个题目块
     */
    private Question parseSingleQuestionBlock(String block, Long subjectId) {
        // 提取题目内容（去掉题号）
        Pattern questionContentPattern = Pattern.compile("^\\d+[\\.、]\\s*(.*?)(?=[A-Z][\\.、]|$)", Pattern.DOTALL);
        Matcher contentMatcher = questionContentPattern.matcher(block);
        
        String questionContent = "";
        if (contentMatcher.find()) {
            questionContent = contentMatcher.group(1).trim();
        }
        
        // 提取选项
        Map<String, String> options = parseOptions(block);
        
        // 提取答案
        Pattern answerPattern = Pattern.compile("【?答案[：:]\\s*([A-Z])|答[：:]\\s*([A-Z])|(?i)answer[：:]\\s*([A-Z])");
        Matcher answerMatcher = answerPattern.matcher(block);
        
        String answer = null;
        if (answerMatcher.find()) {
            answer = answerMatcher.group(1) != null ? answerMatcher.group(1) :
                     answerMatcher.group(2) != null ? answerMatcher.group(2) : answerMatcher.group(3);
        }
        
        // 提取解析
        Pattern analysisPattern = Pattern.compile("【?解析[：:]\\s*(.*?)(?=\\d+[\\.、]|$|[A-Z][\\.、]|答案|$)", Pattern.DOTALL);
        Matcher analysisMatcher = analysisPattern.matcher(block);
        
        String analysis = "";
        if (analysisMatcher.find()) {
            analysis = analysisMatcher.group(1).trim();
        }
        
        // 创建题目对象
        if (!questionContent.isEmpty() && !options.isEmpty()) {
            Question question = new Question();
            question.setContent(questionContent);
            question.setType(com.gwy.model.Question.QuestionType.SINGLE_CHOICE);
            question.setAnalysis(analysis);
            question.setOptions(new ArrayList<>(options.values()));
            
            if (answer != null) {
                List<String> answers = new ArrayList<>();
                answers.add(answer);
                question.setAnswers(answers);
            }
            
            return question;
        }
        
        return null;
    }
}