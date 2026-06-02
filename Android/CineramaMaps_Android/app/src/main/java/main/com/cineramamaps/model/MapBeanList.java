package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MapBeanList {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("subscription_status")
    @Expose
    private String subscription_status;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name_ar")
    @Expose
    private String nameAr;
    @SerializedName("country_icon")
    @Expose
    private String countryIcon;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("date_time")
    @Expose
    private String dateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubscription_status() {
        return subscription_status;
    }

    public void setSubscription_status(String subscription_status) {
        this.subscription_status = subscription_status;
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

    public String getCountryIcon() {
        return countryIcon;
    }

    public void setCountryIcon(String countryIcon) {
        this.countryIcon = countryIcon;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
