package com.gwy.exam.model;

public class User {
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String phone;

    // 构造函数
    public User() {}

    public User(String username, String email, String nickname, String phone) {
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.phone = phone;
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}