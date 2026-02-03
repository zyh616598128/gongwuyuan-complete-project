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
    
    @Column(length = 10000) // 增加长度以支持资料分析题的长文本
    private String analysis; // 解析
    
    @Column(length = 5000) // 用于存储参考资料
    private String referenceMaterial; // 参考资料（用于资料分析题）
    
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
    
    @ManyToOne
    @JoinColumn(name = "parent_question_id") // 用于组合题，子题指向父题
    private Question parentQuestion; // 父题目（用于组合题的子题）
    
    @OneToMany(mappedBy = "parentQuestion", cascade = CascadeType.ALL)
    private List<Question> subQuestions; // 子题目列表（用于组合题）
    
    private Integer sortIndex; // 排序索引，用于组合题中子题的排序
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private Boolean isActive = true;
    
    public enum QuestionType {
        SINGLE_CHOICE, // 单选题
        MULTIPLE_CHOICE, // 多选题
        TRUE_FALSE, // 判断题
        FILL_BLANK, // 填空题
        ESSAY, // 论述题
        MATERIAL_ANALYSIS, // 资料分析题
        COMPOSITE_QUESTION // 组合题
    }
    
    public enum DifficultyLevel {
        EASY, MEDIUM, HARD
    }
}