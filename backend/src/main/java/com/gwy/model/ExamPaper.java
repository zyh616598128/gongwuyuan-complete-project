package com.gwy.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "exam_papers")
@Data
public class ExamPaper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title; // 试卷标题
    
    @Column(length = 1000)
    private String description; // 试卷描述
    
    @ManyToMany
    @JoinTable(
        name = "exam_paper_questions",
        joinColumns = @JoinColumn(name = "exam_paper_id"),
        inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<Question> questions; // 试卷包含的题目
    
    private Integer totalQuestions; // 总题数
    
    private Integer totalTime; // 考试时间（分钟）
    
    private Integer totalScore; // 总分
    
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject; // 所属科目
    
    private Boolean isActive = true;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}