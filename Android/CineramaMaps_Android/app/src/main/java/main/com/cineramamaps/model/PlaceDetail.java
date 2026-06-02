package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceDetail {

    @SerializedName("currentUserFavorite")
    @Expose
    private boolean currentUserFavorite;
    @SerializedName("map_icon")
    @Expose
    private String map_icon;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("fav_status")
    @Expose
    private String fav_status;
    @SerializedName("total_unfav_place")
    @Expose
    private String total_unfav_place;
    @SerializedName("total_fav_place")
    @Expose
    private String total_fav_place;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("placeid")
    @Expose
    private String placeId;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("tag_id")
    @Expose
    private String tagId;
    @SerializedName("place_name")
    @Expose
    private String placeName;
    @SerializedName("place_name_ar")
    @Expose
    private String placeNameAr;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("description_ar")
    @Expose
    private String descriptionAr;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("tag_ar")
    @Expose
    private String tagAr;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("icon_background_color")
    @Expose
    private String iconBackgroundColor;
    @SerializedName("show_only_icon")
    @Expose
    private String showOnlyIcon;
    @SerializedName("promo_code_and_discount")
    @Expose
    private String promoCodeAndDiscount;
    @SerializedName("promo_code_percentage")
    @Expose
    private String promoCodePercentage;
    @SerializedName("suggested_time")
    @Expose
    private String suggestedTime;
    @SerializedName("advice")
    @Expose
    private String advice;
    @SerializedName("advice_arabic")
    @Expose
    private String adviceArabic;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("country_name_ar")
    @Expose
    private String countryNameAr;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("city_name_ar")
    @Expose
    private String cityNameAr;
    @SerializedName("avg_rating")
    @Expose
    private String avgRating;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("plan_purchase_status")
    @Expose
    private String planPurchaseStatus;
    @SerializedName("tag_details")
    @Expose
    private List<TagDetail> tagDetails;
    @SerializedName("colorCode")
    @Expose
    private String colorCode;

    @SerializedName("video_link_en")
    @Expose
    private String video_link_en;

    @SerializedName("video_link_ar")
    @Expose
    private String video_link_ar;

    public String getVideo_link_ar() {
        return video_link_ar;
    }

    public void setVideo_link_ar(String video_link_ar) {
        this.video_link_ar = video_link_ar;
    }

    public String getVideo_link_en() {
        return video_link_en;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setVideo_link_en(String video_link_en) {
        this.video_link_en = video_link_en;
    }

    public String getPromoCodePercentage() {
        return promoCodePercentage;
    }

    public void setPromoCodePercentage(String promoCodePercentage) {
        this.promoCodePercentage = promoCodePercentage;
    }

    public String getSuggestedTime() {
        return suggestedTime;
    }

    public void setSuggestedTime(String suggestedTime) {
        this.suggestedTime = suggestedTime;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getAdviceArabic() {
        return adviceArabic;
    }

    public void setAdviceArabic(String adviceArabic) {
        this.adviceArabic = adviceArabic;
    }

    public String getFav_status() {
        return fav_status;
    }

    public void setFav_status(String fav_status) {
        this.fav_status = fav_status;
    }

    public boolean isCurrentUserFavorite() {
        return currentUserFavorite;
    }

    public void setCurrentUserFavorite(boolean currentUserFavorite) {
        this.currentUserFavorite = currentUserFavorite;
    }

    public String getTotal_fav_place() {
        return total_fav_place;
    }

    public void setTotal_fav_place(String total_fav_place) {
        this.total_fav_place = total_fav_place;
    }

    public String getTotal_unfav_place() {
        return total_unfav_place;
    }

    public void setTotal_unfav_place(String total_unfav_place) {
        this.total_unfav_place = total_unfav_place;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMap_icon() {
        return map_icon;
    }

    public void setMap_icon(String map_icon) {
        this.map_icon = map_icon;
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

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceNameAr() {
        return placeNameAr;
    }

    public void setPlaceNameAr(String placeNameAr) {
        this.placeNameAr = placeNameAr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionAr() {
        return descriptionAr;
    }

    public void setDescriptionAr(String descriptionAr) {
        this.descriptionAr = descriptionAr;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTagAr() {
        return tagAr;
    }

    public void setTagAr(String tagAr) {
        this.tagAr = tagAr;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconBackgroundColor() {
        return iconBackgroundColor;
    }

    public void setIconBackgroundColor(String iconBackgroundColor) {
        this.iconBackgroundColor = iconBackgroundColor;
    }

    public String getShowOnlyIcon() {
        return showOnlyIcon;
    }

    public void setShowOnlyIcon(String showOnlyIcon) {
        this.showOnlyIcon = showOnlyIcon;
    }

    public String getPromoCodeAndDiscount() {
        return promoCodeAndDiscount;
    }

    public void setPromoCodeAndDiscount(String promoCodeAndDiscount) {
        this.promoCodeAndDiscount = promoCodeAndDiscount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryNameAr() {
        return countryNameAr;
    }

    public void setCountryNameAr(String countryNameAr) {
        this.countryNameAr = countryNameAr;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityNameAr() {
        return cityNameAr;
    }

    public void setCityNameAr(String cityNameAr) {
        this.cityNameAr = cityNameAr;
    }

    public String getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(String avgRating) {
        this.avgRating = avgRating;
    }

    public String getPlanPurchaseStatus() {
        return planPurchaseStatus;
    }

    public void setPlanPurchaseStatus(String planPurchaseStatus) {
        this.planPurchaseStatus = planPurchaseStatus;
    }

    public List<TagDetail> getTagDetails() {
        return tagDetails;
    }

    public void setTagDetails(List<TagDetail> tagDetails) {
        this.tagDetails = tagDetails;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }


}
