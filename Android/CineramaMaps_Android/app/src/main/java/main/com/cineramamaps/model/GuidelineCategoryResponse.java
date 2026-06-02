package main.com.cineramamaps.model;

import java.util.List;

public class GuidelineCategoryResponse {
    private List<GuidelineCategoryModel> result;
    private String message;
    private String status;

    public List<GuidelineCategoryModel> getResult() {
        return result;
    }

    public void setResult(List<GuidelineCategoryModel> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

