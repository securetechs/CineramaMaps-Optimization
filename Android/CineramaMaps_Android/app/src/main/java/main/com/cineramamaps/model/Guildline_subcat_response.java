package main.com.cineramamaps.model;

import java.util.List;

public class Guildline_subcat_response {
    private List<GuidelinesSubCategory> result;
    private String message;
    private String status;

    public List<GuidelinesSubCategory> getResult() {
        return result;
    }

    public void setResult(List<GuidelinesSubCategory> result) {
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

    @Override
    public String toString() {
        return "Guildline_subcat_response{" +
                "result=" + result +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
