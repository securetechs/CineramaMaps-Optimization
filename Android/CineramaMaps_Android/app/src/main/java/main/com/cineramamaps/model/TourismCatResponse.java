package main.com.cineramamaps.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TourismCatResponse {
    @SerializedName("result")
    private List<TourismCategory> result;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    public List<TourismCategory> getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
