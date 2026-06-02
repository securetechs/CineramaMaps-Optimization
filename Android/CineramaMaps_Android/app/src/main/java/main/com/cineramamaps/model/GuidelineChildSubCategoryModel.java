package main.com.cineramamaps.model;

public class GuidelineChildSubCategoryModel {
    private String id;
    private String category_id;
    private String sub_cat_id;
    private String name;
    private String name_ar;
    private String date_time;

    // Getters
    public String getId() {
        return id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getSub_cat_id() {
        return sub_cat_id;
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

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public void setSub_cat_id(String sub_cat_id) {
        this.sub_cat_id = sub_cat_id;
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

