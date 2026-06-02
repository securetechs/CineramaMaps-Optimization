package main.com.cineramamaps.model;

public class Category {
    private String id;
    private String category_name;
    private String category_name_ar;
    private String image;
    private String status;
    private String date_time;

    public Category(String id, String category_name, String category_name_ar, String image, String status, String date_time) {
        this.id = id;
        this.category_name = category_name;
        this.category_name_ar = category_name_ar;
        this.image = image;
        this.status = status;
        this.date_time = date_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_name_ar() {
        return category_name_ar;
    }

    public void setCategory_name_ar(String category_name_ar) {
        this.category_name_ar = category_name_ar;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
