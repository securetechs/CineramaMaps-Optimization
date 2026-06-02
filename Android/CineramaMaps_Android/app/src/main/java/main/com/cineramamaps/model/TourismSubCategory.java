package main.com.cineramamaps.model;

import com.google.gson.annotations.SerializedName;

public class TourismSubCategory {

    @SerializedName("id")
    private String id;

    @SerializedName("cat_id")
    private String catId;

    @SerializedName("name")
    private String name;

    @SerializedName("name_ar")
    private String nameAr;

    @SerializedName("date_time")
    private String dateTime;

    public String getId() {
        return id;
    }

    public String getCatId() {
        return catId;
    }

    public String getName() {
        return name;
    }

    public String getNameAr() {
        return nameAr;
    }

    public String getDateTime() {
        return dateTime;
    }
}
