package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tag {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("tag_name")
    @Expose
    private String tagName;
    @SerializedName("tag_name_ar")
    @Expose
    private String tagNameAr;
    @SerializedName("color_code")
    @Expose
    private String colorCode;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("total_tag_place_count")
    @Expose
    private String totalTagPlaceCount;
    @SerializedName("current_user_favorite") // Optional if from backend
    @Expose
    private boolean currentUserFavorite;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagNameAr() {
        return tagNameAr;
    }

    public void setTagNameAr(String tagNameAr) {
        this.tagNameAr = tagNameAr;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTotalTagPlaceCount() {
        return totalTagPlaceCount;
    }

    public void setTotalTagPlaceCount(String totalTagPlaceCount) {
        this.totalTagPlaceCount = totalTagPlaceCount;
    }

    public boolean currentUserFavorite() {
        return currentUserFavorite;
    }

    public void currentUserFavorite(boolean currentUserFavorite) {
        this.currentUserFavorite = currentUserFavorite;
    }

}
