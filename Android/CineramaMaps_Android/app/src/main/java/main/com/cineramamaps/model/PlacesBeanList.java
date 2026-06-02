package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacesBeanList {

    @SerializedName("police_number_name")
    @Expose
    private String police_number_name;
    @SerializedName("police_number_name_ar")
    @Expose
    private String police_number_name_ar;
    @SerializedName("car_police_number_name")
    @Expose
    private String car_police_number_name;
    @SerializedName("car_police_number_name_ar")
    @Expose
    private String car_police_number_name_ar;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("tag_id")
    @Expose
    private String tagId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name_ar")
    @Expose
    private String nameAr;
    @SerializedName("about_city")
    @Expose
    private String aboutCity;
    @SerializedName("about_city_ar")
    @Expose
    private String aboutCityAr;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("tag_ar")
    @Expose
    private String tagAr;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("currency_ar")
    @Expose
    private String currencyAr;
    @SerializedName("clothing")
    @Expose
    private String clothing;
    @SerializedName("clothing_ar")
    @Expose
    private String clothingAr;
    @SerializedName("health")
    @Expose
    private String health;
    @SerializedName("health_ar")
    @Expose
    private String healthAr;
    @SerializedName("communications")
    @Expose
    private String communications;
    @SerializedName("communications_ar")
    @Expose
    private String communicationsAr;
    @SerializedName("offical_language")
    @Expose
    private String officalLanguage;
    @SerializedName("offical_language_ar")
    @Expose
    private String officalLanguageAr;
    @SerializedName("best_time_to_visit")
    @Expose
    private String bestTimeToVisit;
    @SerializedName("best_time_to_visit_ar")
    @Expose
    private String bestTimeToVisitAr;
    @SerializedName("electrical_socket")
    @Expose
    private String electricalSocket;
    @SerializedName("electrical_socket_ar")
    @Expose
    private String electricalSocketAr;
    @SerializedName("the_waether")
    @Expose
    private String theWaether;
    @SerializedName("the_waether_ar")
    @Expose
    private String theWaetherAr;
    @SerializedName("car_police_number")
    @Expose
    private String carPoliceNumber;
    @SerializedName("police_number")
    @Expose
    private String policeNumber;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("tags")
    @Expose
    private List<Tag> tags;
    @SerializedName("places_images")
    @Expose
    private List<PlacesImage> placesImages;
    @SerializedName("rating_review")
    @Expose
    private List<RatingReview> ratingReview;
    @SerializedName("place_details")
    @Expose
    private List<PlaceDetail> placeDetails;
    @SerializedName("currentUserFavorite")
    @Expose
    private static boolean currentUserFavorite;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPolice_number_name() {
        return police_number_name;
    }

    public void setPolice_number_name(String police_number_name) {
        this.police_number_name = police_number_name;
    }

    public String getPolice_number_name_ar() {
        return police_number_name_ar;
    }

    public void setPolice_number_name_ar(String police_number_name_ar) {
        this.police_number_name_ar = police_number_name_ar;
    }

    public String getCar_police_number_name() {
        return car_police_number_name;
    }

    public void setCar_police_number_name(String car_police_number_name) {
        this.car_police_number_name = car_police_number_name;
    }

    public String getCar_police_number_name_ar() {
        return car_police_number_name_ar;
    }

    public void setCar_police_number_name_ar(String car_police_number_name_ar) {
        this.car_police_number_name_ar = car_police_number_name_ar;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
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

    public String getAboutCity() {
        return aboutCity;
    }

    public void setAboutCity(String aboutCity) {
        this.aboutCity = aboutCity;
    }

    public String getAboutCityAr() {
        return aboutCityAr;
    }

    public void setAboutCityAr(String aboutCityAr) {
        this.aboutCityAr = aboutCityAr;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencyAr() {
        return currencyAr;
    }

    public void setCurrencyAr(String currencyAr) {
        this.currencyAr = currencyAr;
    }

    public String getClothing() {
        return clothing;
    }

    public void setClothing(String clothing) {
        this.clothing = clothing;
    }

    public String getClothingAr() {
        return clothingAr;
    }

    public void setClothingAr(String clothingAr) {
        this.clothingAr = clothingAr;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getHealthAr() {
        return healthAr;
    }

    public void setHealthAr(String healthAr) {
        this.healthAr = healthAr;
    }

    public String getCommunications() {
        return communications;
    }

    public void setCommunications(String communications) {
        this.communications = communications;
    }

    public String getCommunicationsAr() {
        return communicationsAr;
    }

    public static boolean isCurrentUserFavorite() {
        return currentUserFavorite;
    }

    public void setCurrentUserFavorite(boolean currentUserFavorite) {
        PlacesBeanList.currentUserFavorite = currentUserFavorite;
    }

    public void setCommunicationsAr(String communicationsAr) {
        this.communicationsAr = communicationsAr;
    }

    public String getOfficalLanguage() {
        return officalLanguage;
    }

    public void setOfficalLanguage(String officalLanguage) {
        this.officalLanguage = officalLanguage;
    }

    public String getOfficalLanguageAr() {
        return officalLanguageAr;
    }

    public void setOfficalLanguageAr(String officalLanguageAr) {
        this.officalLanguageAr = officalLanguageAr;
    }

    public String getBestTimeToVisit() {
        return bestTimeToVisit;
    }

    public void setBestTimeToVisit(String bestTimeToVisit) {
        this.bestTimeToVisit = bestTimeToVisit;
    }

    public String getBestTimeToVisitAr() {
        return bestTimeToVisitAr;
    }

    public void setBestTimeToVisitAr(String bestTimeToVisitAr) {
        this.bestTimeToVisitAr = bestTimeToVisitAr;
    }

    public String getElectricalSocket() {
        return electricalSocket;
    }

    public void setElectricalSocket(String electricalSocket) {
        this.electricalSocket = electricalSocket;
    }

    public String getElectricalSocketAr() {
        return electricalSocketAr;
    }

    public void setElectricalSocketAr(String electricalSocketAr) {
        this.electricalSocketAr = electricalSocketAr;
    }

    public String getTheWaether() {
        return theWaether;
    }

    public void setTheWaether(String theWaether) {
        this.theWaether = theWaether;
    }

    public String getTheWaetherAr() {
        return theWaetherAr;
    }

    public void setTheWaetherAr(String theWaetherAr) {
        this.theWaetherAr = theWaetherAr;
    }

    public String getCarPoliceNumber() {
        return carPoliceNumber;
    }

    public void setCarPoliceNumber(String carPoliceNumber) {
        this.carPoliceNumber = carPoliceNumber;
    }

    public String getPoliceNumber() {
        return policeNumber;
    }

    public void setPoliceNumber(String policeNumber) {
        this.policeNumber = policeNumber;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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

    public List<PlaceDetail> getPlaceDetails() {
        return placeDetails;
    }

    public void setPlaceDetails(List<PlaceDetail> placeDetails) {
        this.placeDetails = placeDetails;
    }
}
