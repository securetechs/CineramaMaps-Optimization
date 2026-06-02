package main.com.cineramamaps.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityResponse {
    @SerializedName("result")
    private List<City> result;

    public List<City> getResult() {
        return result;
    }
}
