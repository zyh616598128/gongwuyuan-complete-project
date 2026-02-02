package com.gwy.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "wrong_questions")
@Data
public class WrongQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
    
    @Column(length = 1000)
    private String userAnswer; // 用户错误答案
    
    @Column(length = 1000)
    private String note; // 用户备注
    
    private Integer errorCount; // 错误次数
    
    private LocalDateTime firstErrorTime; // 首次错误时间
    
    private LocalDateTime lastErrorTime; // 最近错误时间
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}