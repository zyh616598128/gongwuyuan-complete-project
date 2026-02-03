package com.gwy.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;

@Data
public class CreateExamRequest {
    @NotNull(message = "科目ID不能为空")
    private Long subjectId;
    
    @NotBlank(message = "考试标题不能为空")
    private String title;
    
    @Min(value = 1, message = "考试时长至少为1分钟")
    private Integer durationMinutes;
    
    @Min(value = 1, message = "题目数量至少为1题")
    private Integer questionCount;
}