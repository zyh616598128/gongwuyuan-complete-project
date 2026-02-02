package com.gwy.exam.api;

import com.gwy.exam.model.Subject;
import java.util.List;

public class SubjectListResponse {
    private boolean success;
    private String message;
    private List<Subject> data;

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

    public List<Subject> getData() {
        return data;
    }

    public void setData(List<Subject> data) {
        this.data = data;
    }
}