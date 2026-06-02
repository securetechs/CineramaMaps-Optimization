package main.com.cineramamaps.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TourismChildSubCatResponse {

    @SerializedName("result")
    private List<TourismChildSubCategory> result;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    public List<TourismChildSubCategory> getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
