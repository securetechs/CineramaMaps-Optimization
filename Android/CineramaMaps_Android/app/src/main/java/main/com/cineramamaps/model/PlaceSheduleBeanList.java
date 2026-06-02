package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceSheduleBeanList {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("day_name")
    @Expose
    private String dayName;
    @SerializedName("day_name_ar")
    @Expose
    private String dayNameAr;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("day_wise_trip")
    @Expose
    private List<DayWiseTrip> dayWiseTrip;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<DayWiseTrip> getDayWiseTrip() {
        return dayWiseTrip;
    }

    public void setDayWiseTrip(List<DayWiseTrip> dayWiseTrip) {
        this.dayWiseTrip = dayWiseTrip;
    }

}
