package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityMapBeanList {
    @SerializedName("exp_date")
    @Expose
    private String exp_date;

    @SerializedName("youtube_video_link")
    @Expose
    private String youtube_video_link;
    @SerializedName("youtube_video_link_arabic")
    @Expose
    private String youtube_video_link_arabic;

    @SerializedName("map_name")
    @Expose
    private String map_name;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("city_map_price")
    @Expose
    private String city_map_price;
    @SerializedName("city_map_month")
    @Expose
    private String city_map_month;
    @SerializedName("subscription_status")
    @Expose
    private String subscription_status;
    @SerializedName("fav_status")
    @Expose
    private String fav_status;
    @SerializedName("country_details")
    @Expose
    private CountryDetails countryDetails;

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
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("country_name_ar")
    @Expose
    private String countryNameAr;
    @SerializedName("avg_rating")
    @Expose
    private String avgRating;
    @SerializedName("tag_details")
    @Expose
    private List<TagDetail> tagDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    public String getYoutube_video_link() {
        return youtube_video_link;
    }

    public void setYoutube_video_link(String youtube_video_link) {
        this.youtube_video_link = youtube_video_link;
    }

    public String getYoutube_video_link_arabic() {
        return youtube_video_link_arabic;
    }

    public void setYoutube_video_link_arabic(String youtube_video_link_arabic) {
        this.youtube_video_link_arabic = youtube_video_link_arabic;
    }

    public String getMap_name() {
        return map_name;
    }

    public void setMap_name(String map_name) {
        this.map_name = map_name;
    }


    public CountryDetails getCountryDetails() {
        return countryDetails;
    }

    public void setCountryDetails(CountryDetails countryDetails) {
        this.countryDetails = countryDetails;
    }

    public String getCity_map_month() {
        return city_map_month;
    }

    public void setCity_map_month(String city_map_month) {
        this.city_map_month = city_map_month;
    }

    public String getCity_map_price() {
        return city_map_price;
    }

    public void setCity_map_price(String city_map_price) {
        this.city_map_price = city_map_price;
    }

    public String getSubscription_status() {
        return subscription_status;
    }

    public void setSubscription_status(String subscription_status) {
        this.subscription_status = subscription_status;
    }

    public String getFav_status() {
        return fav_status;
    }

    public void setFav_status(String fav_status) {
        this.fav_status = fav_status;
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
}
