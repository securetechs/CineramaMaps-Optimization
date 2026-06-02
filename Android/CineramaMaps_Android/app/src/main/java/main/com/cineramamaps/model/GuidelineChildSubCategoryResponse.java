package main.com.cineramamaps.model;

import java.util.List;

public class GuidelineChildSubCategoryResponse {
    private List<GuidelineChildSubCategoryModel> result;
    private String message;
    private String status;

    public List<GuidelineChildSubCategoryModel> getResult() {
        return result;
    }

    public void setResult(List<GuidelineChildSubCategoryModel> result) {
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

