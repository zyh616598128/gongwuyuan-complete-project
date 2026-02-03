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
        
        // 预处理文本，确保格式统一
        text = preprocessText(text);
        
        // 尝试多种题目匹配模式
        List<Pattern> questionPatterns = Arrays.asList(
            // 模式1: 数字. 题目内容 A.选项 B.选项 C.选项 D.选项
            Pattern.compile("(\\d+)[\\.．、]\\s*(.+?)(?=(?:[Aa][\\.．、:]|\\b[Aa]\\b|\\b[Aa]\\.|答案|解析|【答案】|【解析】|\\d+[\\.．、]|$))", Pattern.DOTALL),
            
            // 模式2: 数字、题目内容 A.选项 B.选项 C.选项 D.选项
            Pattern.compile("(\\d+)[、]\\s*(.+?)(?=(?:[Aa][\\.．、:]|\\b[Aa]\\b|\\b[Aa]\\.|答案|解析|【答案】|【解析】|\\d+[、]|$))", Pattern.DOTALL),
            
            // 模式3: 数字. 题目内容 (A)选项 (B)选项 (C)选项 (D)选项
            Pattern.compile("(\\d+)[\\.．、]\\s*(.+?)(?=(?:\\([Aa]\\)|\\d+[\\.．、]|答案|解析|【答案】|【解析】|$))", Pattern.DOTALL)
        );
        
        // 存储已处理的文本位置，避免重复处理
        Set<Integer> processedPositions = new HashSet<>();
        
        for (Pattern pattern : questionPatterns) {
            Matcher matcher = pattern.matcher(text);
            
            while (matcher.find()) {
                int startPosition = matcher.start();
                
                // 如果这个位置已经被处理过，则跳过
                if (processedPositions.contains(startPosition)) {
                    continue;
                }
                
                try {
                    String questionNum = matcher.group(1);
                    String questionContent = matcher.group(2).trim();
                    
                    // 获取选项和答案的上下文
                    int questionEnd = matcher.end();
                    int nextQuestionStart = findNextQuestion(text, questionEnd);
                    
                    String contextAfter = text.substring(questionEnd, nextQuestionStart);
                    
                    // 提取选项
                    Map<String, String> optionsMap = parseOptions(contextAfter);
                    
                    // 如果当前模式没有提取到足够的选项，尝试扩展上下文
                    if (optionsMap.size() < 2) {
                        // 扩展上下文以捕获更多选项
                        int extendedEnd = Math.min(nextQuestionStart + 1000, text.length());
                        String extendedContext = text.substring(questionEnd, extendedEnd);
                        optionsMap = parseOptions(extendedContext);
                    }
                    
                    // 如果仍然没有足够选项，尝试解析整个题目块
                    if (optionsMap.size() < 2) {
                        // 查找直到下一个题号的全部内容
                        String fullContext = text.substring(matcher.start(), nextQuestionStart);
                        optionsMap = extractOptionsFromFullContext(fullContext);
                    }
                    
                    // 提取答案
                    String answer = extractAnswer(text, questionEnd, nextQuestionStart);
                    
                    // 如果当前模式没有找到答案，尝试在整个题目文本中查找
                    if (answer == null || answer.isEmpty()) {
                        String fullQuestionText = text.substring(matcher.start(), nextQuestionStart);
                        answer = extractAnswerFromFullText(fullQuestionText);
                    }
                    
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
                        processedPositions.add(startPosition);
                    }
                } catch (Exception e) {
                    // 跳过有问题的题目，继续处理下一个
                    continue;
                }
            }
        }
        
        // 如果上述模式都没有找到题目，尝试使用更宽松的模式
        if (questions.isEmpty()) {
            questions = parseWithRelaxedPattern(text, subjectId);
        }
        
        return questions;
    }
    
    /**
     * 预处理文本以标准化格式
     */
    private String preprocessText(String text) {
        if (text == null) {
            return "";
        }
        
        // 统一换行符
        text = text.replace("\r\n", "\n").replace("\r", "\n");
        
        // 替换各种句号和分隔符
        text = text.replace("。。", "。").replace("...", "…");
        
        // 统一空格
        text = text.replaceAll("[ \t]+", " ");
        
        // 移除多余的换行，但保留段落结构
        text = text.replaceAll("\n[ \t]*\n[ \t]*\n+", "\n\n");
        
        return text.trim();
    }
    
    /**
     * 查找下一个题目的开始位置
     */
    private int findNextQuestion(String text, int startPosition) {
        Pattern nextQuestionPattern = Pattern.compile("\\d+[\\.．、，]");
        Matcher nextMatcher = nextQuestionPattern.matcher(text.substring(startPosition));
        
        if (nextMatcher.find()) {
            return startPosition + nextMatcher.start();
        }
        
        return text.length();
    }
    
    /**
     * 从完整上下文中提取选项
     */
    private Map<String, String> extractOptionsFromFullContext(String context) {
        Map<String, String> options = new LinkedHashMap<>();
        
        // 匹配多种选项格式：A. B. C. D. 或 A、 B、 C、 D、 或 (A) (B) (C) (D)
        Pattern optionPattern = Pattern.compile(
            "([A-Da-d])([\\.．、:\\uff1a\\)\\(])([^A-Da-d\\(\\)]*(?=([A-Da-d][\\.．、:\\uff1a\\)\\(]|$)))",
            Pattern.DOTALL
        );
        
        Matcher matcher = optionPattern.matcher(context);
        
        while (matcher.find()) {
            String optionLetter = matcher.group(1).toUpperCase();
            String separator = matcher.group(2);
            String optionText = matcher.group(3).trim();
            
            // 进一步清理选项文本
            if (optionText.length() > 50) { // 如果选项文本过长，可能是匹配错误
                continue;
            }
            
            if (!optionText.isEmpty() && !options.containsKey(optionLetter)) {
                options.put(optionLetter, optionText);
            }
        }
        
        return options;
    }
    
    /**
     * 从完整文本中提取答案
     */
    private String extractAnswerFromFullText(String text) {
        // 多种答案格式的匹配模式
        Pattern[] answerPatterns = {
            Pattern.compile("【?答案[：:]\\s*[是为]?\\s*([A-Da-d])", Pattern.CASE_INSENSITIVE),
            Pattern.compile("【?答案[：:]\\s*([A-Da-d])?[，,]?\\s*[是为]?\\s*[A-Da-d]", Pattern.CASE_INSENSITIVE), // Handles "答案：A，正确答案B" cases
            Pattern.compile("答[：:]\\s*[是为]?\\s*([A-Da-d])", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(?i)answer[：:]\\s*[是为]?\\s*([A-Da-d])"),
            Pattern.compile("([A-Da-d])[\\.．、:\\uff1a]\\s*[是为]?[正确对]", Pattern.CASE_INSENSITIVE),
            Pattern.compile("正确答案[：:]\\s*[是为]?\\s*([A-Da-d])", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\b([A-Da-d])\\b[，,]?\\s*[是为]?\\s*[正确对]", Pattern.CASE_INSENSITIVE), // Standalone answer followed by "是正确"
            // 最后尝试简单匹配：在"答案"之后的字母
            Pattern.compile("答案.*?([A-Da-d])", Pattern.CASE_INSENSITIVE)
        };
        
        for (Pattern pattern : answerPatterns) {
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                String result = matcher.group(1);
                if (result != null) {
                    return result.toUpperCase();
                }
            }
        }
        
        return null;
    }
    
    /**
     * 使用宽松模式解析
     */
    private List<Question> parseWithRelaxedPattern(String text, Long subjectId) {
        List<Question> questions = new ArrayList<>();
        
        // 使用非常宽松的模式查找可能的题目
        Pattern relaxedPattern = Pattern.compile(
            "(\\d+)[\\.．、，]\\s*(.+?)(?=\\d+[\\.．、，]|$)",
            Pattern.DOTALL
        );
        
        Matcher matcher = relaxedPattern.matcher(text);
        
        while (matcher.find()) {
            try {
                String questionNum = matcher.group(1);
                String potentialQuestion = matcher.group(2).trim();
                
                // 检查是否包含选项标识
                if (containsOptionMarkers(potentialQuestion)) {
                    Question question = createQuestionFromRelaxedMatch(potentialQuestion, subjectId);
                    if (question != null) {
                        questions.add(question);
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
        
        return questions;
    }
    
    /**
     * 检查文本是否包含选项标记
     */
    private boolean containsOptionMarkers(String text) {
        return text.matches("(?s).*[Aa][\\.．、:\\uff1a\\)\\(].*") ||
               text.matches("(?s).*[Bb][\\.．、:\\uff1a\\)\\(].*") ||
               text.matches("(?s).*[Cc][\\.．、:\\uff1a\\)\\(].*") ||
               text.matches("(?s).*[Dd][\\.．、:\\uff1a\\)\\(].*");
    }
    
    /**
     * 从宽松匹配创建题目
     */
    private Question createQuestionFromRelaxedMatch(String text, Long subjectId) {
        // 尝试提取选项和答案
        Map<String, String> options = parseOptions(text);
        
        if (options.size() < 2) {
            return null;
        }
        
        // 提取题目内容（去除选项部分）
        String questionContent = extractQuestionContent(text, options);
        
        if (questionContent.isEmpty()) {
            return null;
        }
        
        String answer = extractAnswerFromFullText(text);
        
        Question question = new Question();
        question.setContent(questionContent);
        question.setType(com.gwy.model.Question.QuestionType.SINGLE_CHOICE);
        question.setSubject(null); // 需要通过subjectId获取
        question.setOptions(new ArrayList<>(options.values()));
        
        if (answer != null && !answer.isEmpty()) {
            List<String> answers = new ArrayList<>();
            answers.add(answer.toUpperCase());
            question.setAnswers(answers);
        }
        
        return question;
    }
    
    /**
     * 从包含选项的文本中提取题目内容
     */
    private String extractQuestionContent(String fullText, Map<String, String> options) {
        String content = fullText;
        
        // 移除已识别的选项
        for (Map.Entry<String, String> entry : options.entrySet()) {
            String optionText = entry.getValue();
            if (!optionText.isEmpty()) {
                content = content.replace(optionText, "");
            }
        }
        
        // 清理剩余的选项标记
        content = content.replaceAll("[A-Da-d][\\.．、:\\uff1a\\)\\(]", "");
        
        return content.trim();
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