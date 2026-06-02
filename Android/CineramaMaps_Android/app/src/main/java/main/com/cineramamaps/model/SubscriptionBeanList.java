package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubscriptionBeanList {
    @SerializedName("is_select")
    @Expose
    private String is_select;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name_ar")
    @Expose
    private String nameAr;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("description_ar")
    @Expose
    private String descriptionAr;
    @SerializedName("month")
    @Expose
    private String month;
    @SerializedName("month_ar")
    @Expose
    private String monthAr;
    @SerializedName("offer_image")
    @Expose
    private String offerImage;
    @SerializedName("offer_percentage")
    @Expose
    private String offerPercentage;
    @SerializedName("offer_description")
    @Expose
    private String offerDescription;
    @SerializedName("offer_description_ar")
    @Expose
    private String offerDescriptionAr;
    @SerializedName("remove_status")
    @Expose
    private String removeStatus;
    @SerializedName("date_time")
    @Expose
    private String dateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_select() {
        return is_select;
    }

    public void setIs_select(String is_select) {
        this.is_select = is_select;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionAr() {
        return descriptionAr;
    }

    public void setDescriptionAr(String descriptionAr) {
        this.descriptionAr = descriptionAr;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonthAr() {
        return monthAr;
    }

    public void setMonthAr(String monthAr) {
        this.monthAr = monthAr;
    }

    public String getOfferImage() {
        return offerImage;
    }

    public void setOfferImage(String offerImage) {
        this.offerImage = offerImage;
    }

    public String getOfferPercentage() {
        return offerPercentage;
    }

    public void setOfferPercentage(String offerPercentage) {
        this.offerPercentage = offerPercentage;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public String getOfferDescriptionAr() {
        return offerDescriptionAr;
    }

    public void setOfferDescriptionAr(String offerDescriptionAr) {
        this.offerDescriptionAr = offerDescriptionAr;
    }

    public String getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(String removeStatus) {
        this.removeStatus = removeStatus;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
