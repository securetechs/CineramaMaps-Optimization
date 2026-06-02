package main.com.cineramamaps.model;

public class GuidelineSubCategoryModel {
    private String id;
    private String cat_id;
    private String name;
    private String name_ar;
    private String date_time;

    public GuidelineSubCategoryModel(String id, String cat_id, String name, String name_ar, String date_time) {
        this.id = id;
        this.cat_id = cat_id;
        this.name = name;
        this.name_ar = name_ar;
        this.date_time = date_time;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getCat_id() {
        return cat_id;
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

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
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
