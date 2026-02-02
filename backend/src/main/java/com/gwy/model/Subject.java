package com.gwy.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subjects")
@Data
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name; // 科目名称
    
    @Column(length = 1000)
    private String description; // 科目描述
    
    private Integer sortOrder; // 排序
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private Boolean isActive = true;
}