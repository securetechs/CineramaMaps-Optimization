package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryBeanList {


    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("date_time")
    @Expose
    private String dateTime;

    @SerializedName("category_added")
    @Expose
    private String categoryAdded;

    public String getCategoryAdded() {
        return categoryAdded;
    }

    public void setCategoryAdded(String categoryAdded) {
        this.categoryAdded = categoryAdded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


}
