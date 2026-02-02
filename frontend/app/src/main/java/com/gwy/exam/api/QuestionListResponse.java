package com.gwy.exam.api;

import com.gwy.exam.model.Question;
import java.util.List;

public class QuestionListResponse {
    private boolean success;
    private String message;
    private List<Question> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Question> getData() {
        return data;
    }

    public void setData(List<Question> data) {
        this.data = data;
    }
}