package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SinglePlaceDetails {

    @SerializedName("site_url")
    @Expose
    private String site_url;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("id")
    @Expose
    private String id;
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
    @SerializedName("promo_code_and_discount")
    @Expose
    private String promoCodeAndDiscount;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("fav_status")
    @Expose
    private String favStatus;
    @SerializedName("total_unfav_place")
    @Expose
    private String totalUnfavPlace;
    @SerializedName("total_fav_place")
    @Expose
    private String totalFavPlace;
    @SerializedName("country_details")
    @Expose
    private CountryDetails countryDetails;
    @SerializedName("city_details")
    @Expose
    private CityDetails cityDetails;
    @SerializedName("avg_rating")
    @Expose
    private String avgRating;
    @SerializedName("tag_details")
    @Expose
    private List<TagDetail> tagDetails;
    @SerializedName("places_images")
    @Expose
    private List<PlacesImage> placesImages;
    @SerializedName("rating_review")
    @Expose
    private List<RatingReview> ratingReview;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSite_url() {
        return site_url;
    }

    public void setSite_url(String site_url) {
        this.site_url = site_url;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }

    public String getTotalUnfavPlace() {
        return totalUnfavPlace;
    }

    public void setTotalUnfavPlace(String totalUnfavPlace) {
        this.totalUnfavPlace = totalUnfavPlace;
    }

    public String getTotalFavPlace() {
        return totalFavPlace;
    }

    public void setTotalFavPlace(String totalFavPlace) {
        this.totalFavPlace = totalFavPlace;
    }

    public CountryDetails getCountryDetails() {
        return countryDetails;
    }

    public void setCountryDetails(CountryDetails countryDetails) {
        this.countryDetails = countryDetails;
    }

    public CityDetails getCityDetails() {
        return cityDetails;
    }

    public void setCityDetails(CityDetails cityDetails) {
        this.cityDetails = cityDetails;
    }

    public String getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(String avgRating) {
        this.avgRating = avgRating;
    }

    public List<TagDetail> getTagDetails() {
        return tagDetails;
    }

    public void setTagDetails(List<TagDetail> tagDetails) {
        this.tagDetails = tagDetails;
    }

    public List<PlacesImage> getPlacesImages() {
        return placesImages;
    }

    public void setPlacesImages(List<PlacesImage> placesImages) {
        this.placesImages = placesImages;
    }

    public List<RatingReview> getRatingReview() {
        return ratingReview;
    }

    public void setRatingReview(List<RatingReview> ratingReview) {
        this.ratingReview = ratingReview;
    }
}
