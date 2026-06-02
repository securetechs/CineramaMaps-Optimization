package main.com.cineramamaps.model;

import java.util.List;

public class GuidelineSubCategoryResponse {

    private List<GuidelineSubCategoryModel> result;
    private String message;
    private String status;

    public List<GuidelineSubCategoryModel> getResult() {
        return result;
    }

    public void setResult(List<GuidelineSubCategoryModel> result) {
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
