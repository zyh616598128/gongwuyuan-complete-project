package com.gwy.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SubmitExamRequest {
    private List<AnswerDto> answers;
    
    @Data
    public static class AnswerDto {
        @NotNull(message = "题目ID不能为空")
        private Long questionId;
        
        @NotNull(message = "选项ID不能为空")
        private String selectedOptionId;
    }
}