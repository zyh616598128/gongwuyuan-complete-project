package com.gwy.exam.model;

public class Subject {
    private Long id;
    private String name; // 科目名称
    private String description; // 科目描述

    // 构造函数
    public Subject() {}

    public Subject(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}