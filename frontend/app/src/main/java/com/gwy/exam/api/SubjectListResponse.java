package com.gwy.exam.api;

import com.gwy.exam.model.Subject;
import java.util.List;

public class SubjectListResponse {
    private boolean success;
    private String message;
    private List<Subject> data;
    private int total;

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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}