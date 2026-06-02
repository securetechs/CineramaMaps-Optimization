package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DayBeanList {
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
}
