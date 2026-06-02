package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripBeanList {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("map_name")
    @Expose
    private String map_name;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("day_id")
    @Expose
    private String dayId;
    @SerializedName("day_name")
    @Expose
    private String dayName;
    @SerializedName("day_name_ar")
    @Expose
    private String dayNameAr;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("country_name_ar")
    @Expose
    private String countryNameAr;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("trip_name")
    @Expose
    private String tripName;
    @SerializedName("map_type")
    @Expose
    private String mapType;
    @SerializedName("map_type_ar")
    @Expose
    private String mapTypeAr;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("how_much_day")
    @Expose
    private String howMuchDay;
    @SerializedName("trip_by_cineramap")
    @Expose
    private String tripByCineramap;
    @SerializedName("date_time")
    @Expose
    private String dateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMap_name() {
        return map_name;
    }

    public void setMap_name(String map_name) {
        this.map_name = map_name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDayId() {
        return dayId;
    }

    public void setDayId(String dayId) {
        this.dayId = dayId;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDayNameAr() {
        return dayNameAr;
    }

    public void setDayNameAr(String dayNameAr) {
        this.dayNameAr = dayNameAr;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
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

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public String getMapTypeAr() {
        return mapTypeAr;
    }

    public void setMapTypeAr(String mapTypeAr) {
        this.mapTypeAr = mapTypeAr;
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

    public String getHowMuchDay() {
        return howMuchDay;
    }

    public void setHowMuchDay(String howMuchDay) {
        this.howMuchDay = howMuchDay;
    }

    public String getTripByCineramap() {
        return tripByCineramap;
    }

    public void setTripByCineramap(String tripByCineramap) {
        this.tripByCineramap = tripByCineramap;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

}
