package com.gwy.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "exam_records")
@Data
public class ExamRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "exam_paper_id", nullable = false)
    private ExamPaper examPaper;
    
    private Integer totalScore;
    
    private Integer obtainedScore;
    
    @ElementCollection
    @CollectionTable(name = "exam_answers", joinColumns = @JoinColumn(name = "exam_record_id"))
    @MapKeyColumn(name = "question_id")
    @Column(name = "answer")
    private List<String> answers; // 用户的答案
    
    @ElementCollection
    @CollectionTable(name = "exam_answer_details", joinColumns = @JoinColumn(name = "exam_record_id"))
    @MapKeyColumn(name = "question_id")
    @Column(name = "is_correct")
    private List<Boolean> correctness; // 答题正确性
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    private Long duration; // 考试用时（秒）
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}