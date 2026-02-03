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
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传的文件不能为空");
        }
        
        PDDocument document = null;
        try {
            document = PDDocument.load(file.getBytes());
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            
            // 清理文本内容，移除多余空格和特殊字符
            text = cleanTextContent(text);
            
            return parseTextQuestions(text, subjectId);
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }
    
    /**
     * 清理文本内容
     */
    private String cleanTextContent(String text) {
        if (text == null) {
            return "";
        }
        
        // 替换多余的空白字符
        text = text.replaceAll("[\\r\\n\\t]+", " ");
        // 替换多个连续空格为单个空格
        text = text.replaceAll("\\s+", " ");
        // 修剪首尾空格
        return text.trim();
    }

    /**
     * 通用文本解析方法
     */
    public List<Question> parseTextQuestions(String text, Long subjectId) {
        List<Question> questions = new ArrayList<>();
        
        if (text == null || text.isEmpty()) {
            return questions;
        }
        
        // 使用更灵活的模式匹配，支持多种题号格式
        Pattern questionPattern = Pattern.compile(
            "(\\d+)[\\.．、](\\s*)(.+?)(?=(?:[Aa][\\.．、]|\\bA\\b|\\bA\\.[ \\t])|(?=\\n\\s*\\d+[\\.．、])|(?=\\d+[\\.．、]\\s)|$)",
            Pattern.DOTALL
        );
        
        Matcher matcher = questionPattern.matcher(text);
        
        while (matcher.find()) {
            try {
                String questionNum = matcher.group(1);
                String questionContent = matcher.group(3).trim();
                
                // 在当前匹配的范围内寻找选项
                int start = matcher.end();
                int end = text.length();
                
                // 寻找下一个题号的位置
                Pattern nextQuestionPattern = Pattern.compile("\\d+[\\.．、]");
                Matcher nextMatcher = nextQuestionPattern.matcher(text.substring(start));
                if (nextMatcher.find()) {
                    end = start + nextMatcher.start();
                }
                
                String contextAfter = text.substring(start, end);
                
                // 提取选项
                Map<String, String> optionsMap = parseOptions(contextAfter);
                
                // 提取答案（在题目内容和选项后搜索）
                String answer = extractAnswer(text, matcher.end(), end);
                
                // 创建题目对象
                if (!questionContent.isEmpty() && !optionsMap.isEmpty()) {
                    Question question = new Question();
                    question.setContent(questionContent);
                    question.setType(com.gwy.model.Question.QuestionType.SINGLE_CHOICE);
                    question.setSubject(null); // 需要通过subjectId获取
                    question.setOptions(new ArrayList<>(optionsMap.values()));
                    
                    if (answer != null && !answer.isEmpty()) {
                        List<String> answers = new ArrayList<>();
                        answers.add(answer.toUpperCase());
                        question.setAnswers(answers);
                    }
                    
                    questions.add(question);
                }
            } catch (Exception e) {
                // 跳过有问题的题目，继续处理下一个
                continue;
            }
        }
        
        return questions;
    }
    
    /**
     * 从文本中提取答案
     */
    private String extractAnswer(String text, int startPos, int endPos) {
        String context = text.substring(startPos, Math.min(endPos, text.length()));
        
        // 多种答案格式的匹配模式
        Pattern[] answerPatterns = {
            Pattern.compile("【?答案[：:]\\s*([A-Da-d])", Pattern.CASE_INSENSITIVE),
            Pattern.compile("答[：:]\\s*([A-Da-d])", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(?i)answer[：:]\\s*([A-Da-d])"),
            Pattern.compile("([A-Da-d])[\\.．、]\\s*是[正确对]", Pattern.CASE_INSENSITIVE),
            Pattern.compile("正确答案[：:]\\s*([A-Da-d])", Pattern.CASE_INSENSITIVE)
        };
        
        for (Pattern pattern : answerPatterns) {
            Matcher matcher = pattern.matcher(context);
            if (matcher.find()) {
                return matcher.group(1).toUpperCase();
            }
        }
        
        return null;
    }

    /**
     * 解析选项文本
     */
    private Map<String, String> parseOptions(String optionsText) {
        Map<String, String> options = new LinkedHashMap<>();
        
        if (optionsText == null || optionsText.isEmpty()) {
            return options;
        }
        
        // 更灵活的选项匹配模式，支持多种格式
        Pattern optionPattern = Pattern.compile(
            "([A-Da-d])[\\.．、:\\uff1a]([^A-Da-d]+?)(?=(?:[A-Da-d][\\.．、:\\uff1a]|\\n|$))",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
        );
        
        Matcher matcher = optionPattern.matcher(optionsText);
        
        while (matcher.find()) {
            String optionLetter = matcher.group(1).toUpperCase();
            String optionText = matcher.group(2).trim();
            
            // 进一步清理选项文本
            if (optionText.endsWith("。") || optionText.endsWith(".") || optionText.endsWith("；")) {
                optionText = optionText.substring(0, optionText.length() - 1);
            }
            
            if (!optionText.isEmpty()) {
                options.put(optionLetter, optionText);
            }
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