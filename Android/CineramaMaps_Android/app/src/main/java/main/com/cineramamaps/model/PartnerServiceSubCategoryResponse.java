package main.com.cineramamaps.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PartnerServiceSubCategoryResponse {

    @SerializedName("result")
    private List<PartnerServiceSubCategory> result;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    public List<PartnerServiceSubCategory> getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
