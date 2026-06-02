package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceBeanNewData {
    @SerializedName("google_map_link")
    @Expose
    private String google_map_link;
    @SerializedName("start_date")
    @Expose
    private String start_date;
    @SerializedName("end_date")
    @Expose
    private String end_date;
    @SerializedName("video_link_en")
    @Expose
    private String video_link_en;

    @SerializedName("video_link_ar")
    @Expose
    private String video_link_ar;

    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("placeid")
    @Expose
    private String placeid;
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
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("site_url")
    @Expose
    private String siteUrl;
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
    @SerializedName("google_map")
    @Expose
    private GoogleMap googleMap;
    private boolean currentUserFavorite;

    public boolean isCurrentUserFavorite() {
        return currentUserFavorite;
    }

    public void setCurrentUserFavorite(boolean currentUserFavorite) {
        this.currentUserFavorite = currentUserFavorite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getGoogle_map_link() {
        return google_map_link;
    }

    public void setGoogle_map_link(String google_map_link) {
        this.google_map_link = google_map_link;
    }

    public String getVideo_link_en() {
        return video_link_en;
    }

    public void setVideo_link_en(String video_link_en) {
        this.video_link_en = video_link_en;
    }

    public String getVideo_link_ar() {
        return video_link_ar;
    }

    public void setVideo_link_ar(String video_link_ar) {
        this.video_link_ar = video_link_ar;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
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

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
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

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
}
