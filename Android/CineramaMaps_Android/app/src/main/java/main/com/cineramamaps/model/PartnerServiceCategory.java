package main.com.cineramamaps.model;

import com.google.gson.annotations.SerializedName;

public class PartnerServiceCategory {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("name_ar")
    private String nameAr;

    @SerializedName("date_time")
    private String dateTime;

    public PartnerServiceCategory(String id, String name, String nameAr, String dateTime) {
        this.id = id;
        this.name = name;
        this.nameAr = nameAr;
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
