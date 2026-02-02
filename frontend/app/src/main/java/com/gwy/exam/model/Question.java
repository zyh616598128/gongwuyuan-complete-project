package com.gwy.exam.model;

import java.util.List;

public class Question {
    private Long id;
    private String content; // 题目内容
    private String analysis; // 解析
    private String type; // 题目类型
    private String difficulty; // 难度级别
    private List<String> options; // 选项列表
    private List<String> answers; // 答案列表
    private String subject; // 所属科目
    private String category; // 题目分类
    private int correctRate; // 正确率
    private boolean isAnswered; // 是否已答
    private boolean isCorrect; // 是否答对
    private String userAnswer; // 用户答案
    private boolean isMarked; // 是否标记

    public Question() {}

    public Question(Long id, String content, String analysis, String type, String difficulty,
                    List<String> options, List<String> answers, String subject, String category,
                    int correctRate, boolean isAnswered, boolean isCorrect, String userAnswer, boolean isMarked) {
        this.id = id;
        this.content = content;
        this.analysis = analysis;
        this.type = type;
        this.difficulty = difficulty;
        this.options = options;
        this.answers = answers;
        this.subject = subject;
        this.category = category;
        this.correctRate = correctRate;
        this.isAnswered = isAnswered;
        this.isCorrect = isCorrect;
        this.userAnswer = userAnswer;
        this.isMarked = isMarked;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAnalysis() { return analysis; }
    public void setAnalysis(String analysis) { this.analysis = analysis; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public List<String> getAnswers() { return answers; }
    public void setAnswers(List<String> answers) { this.answers = answers; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getCorrectRate() { return correctRate; }
    public void setCorrectRate(int correctRate) { this.correctRate = correctRate; }

    public boolean isAnswered() { return isAnswered; }
    public void setAnswered(boolean answered) { isAnswered = answered; }

    public boolean isCorrect() { return isCorrect; }
    public void setCorrect(Boolean correct) { isCorrect = correct; }

    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }

    public boolean isMarked() { return isMarked; }
    public void setMarked(boolean marked) { isMarked = marked; }
}