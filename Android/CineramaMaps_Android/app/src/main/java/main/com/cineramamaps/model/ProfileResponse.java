package main.com.cineramamaps.model;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
    @SerializedName("result")
    private UserDetails result;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;

    public UserDetails getResult() {
        return result;
    }

    public void setResult(UserDetails result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getStatus() {
        return status.equals("1");
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
