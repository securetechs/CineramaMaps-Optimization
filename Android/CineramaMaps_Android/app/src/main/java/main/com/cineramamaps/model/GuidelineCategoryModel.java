package main.com.cineramamaps.model;

public class GuidelineCategoryModel {
    private String id;
    private String city_id;
    private String name;
    private String name_ar;
    private String date_time;


    public GuidelineCategoryModel(String id, String city_id, String name, String name_ar, String date_time) {
        this.id = id;
        this.city_id = city_id;
        this.name = name;
        this.name_ar = name_ar;
        this.date_time = date_time;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getCity_id() {
        return city_id;
    }

    public String getName() {
        return name;
    }

    public String getName_ar() {
        return name_ar;
    }

    public String getDate_time() {
        return date_time;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
