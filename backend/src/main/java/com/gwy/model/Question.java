package com.gwy.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "questions")
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 5000, nullable = false)
    private String content; // 题目内容
    
    @Column(length = 1000)
    private String analysis; // 解析
    
    @Enumerated(EnumType.STRING)
    private QuestionType type; // 题目类型
    
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficulty; // 难度级别
    
    @ElementCollection
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option_text")
    private List<String> options; // 选项列表
    
    @ElementCollection
    @CollectionTable(name = "question_answers", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "answer")
    private List<String> answers; // 答案列表（多选题可能有多个答案）
    
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject; // 所属科目
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category; // 题目分类
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private Boolean isActive = true;
    
    public enum QuestionType {
        SINGLE_CHOICE, // 单选题
        MULTIPLE_CHOICE, // 多选题
        TRUE_FALSE, // 判断题
        FILL_BLANK, // 填空题
        ESSAY // 论述题
    }
    
    public enum DifficultyLevel {
        EASY, MEDIUM, HARD
    }
}