package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatBean {
    @SerializedName("result")
    @Expose
    private List<ChatListBean> result = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("finder_status")
    @Expose
    private String finder_status;

    public String getFinder_status() {
        return finder_status;
    }

    public void setFinder_status(String finder_status) {
        this.finder_status = finder_status;
    }

    public List<ChatListBean> getResult() {
        return result;
    }

    public void setResult(List<ChatListBean> result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
