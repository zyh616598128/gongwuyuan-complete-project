package com.gwy.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name; // 分类名称
    
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent; // 父分类
    
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject; // 所属科目
    
    @Column(length = 1000)
    private String description; // 分类描述
    
    private Integer sortOrder; // 排序
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private Boolean isActive = true;
}