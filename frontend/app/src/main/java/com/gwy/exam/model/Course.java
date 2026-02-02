package com.gwy.exam.model;

public class Course {
    private Long id;
    private String title;
    private String description;
    private String coverImageUrl;
    private String teacherName;
    private double price;
    private int duration; // 课时数
    private int lessonCount; // 讲数
    private String category;
    private String type; // video, live, etc.
    private double rating;
    private int studentCount;
    private boolean isFavorite;
    private boolean isFree;

    public Course() {}

    public Course(Long id, String title, String description, String coverImageUrl, 
                  String teacherName, double price, int duration, int lessonCount, 
                  String category, String type, double rating, int studentCount, 
                  boolean isFree) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.coverImageUrl = coverImageUrl;
        this.teacherName = teacherName;
        this.price = price;
        this.duration = duration;
        this.lessonCount = lessonCount;
        this.category = category;
        this.type = type;
        this.rating = rating;
        this.studentCount = studentCount;
        this.isFavorite = false;
        this.isFree = isFree;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public int getLessonCount() { return lessonCount; }
    public void setLessonCount(int lessonCount) { this.lessonCount = lessonCount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getStudentCount() { return studentCount; }
    public void setStudentCount(int studentCount) { this.studentCount = studentCount; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public boolean isFree() { return isFree; }
    public void setFree(boolean free) { isFree = free; }
}