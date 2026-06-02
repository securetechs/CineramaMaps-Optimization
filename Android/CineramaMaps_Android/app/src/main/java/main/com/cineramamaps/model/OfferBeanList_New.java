package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OfferBeanList_New {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_name_ar")
    @Expose
    private String categoryNameAr;
    //    @SerializedName("image")
//    @Expose
//    private String image;
    @SerializedName("status")
    @Expose
    private String status;
//    @SerializedName("date_time")
//    @Expose
//    private String dateTime;
//    @SerializedName("company_offer")
//    @Expose
//    private List<CompanyOffer> companyOffer;

    //    @SerializedName("id")
//    @Expose
//    private String id;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("company_name_ar")
    @Expose
    private String companyNameAr;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("description_ar")
    @Expose
    private String descriptionAr;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("discount_percentage")
    @Expose
    private String discountPercentage;
    @SerializedName("discount_code")
    @Expose
    private String discountCode;
    @SerializedName("date_time")
    @Expose
    private String dateTime;


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

    public String getCategoryNameAr() {
        return categoryNameAr;
    }

    public void setCategoryNameAr(String categoryNameAr) {
        this.categoryNameAr = categoryNameAr;
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

//    public List<CompanyOffer> getCompanyOffer() {
//        return companyOffer;
//    }
//
//    public void setCompanyOffer(List<CompanyOffer> companyOffer) {
//        this.companyOffer = companyOffer;
//    }


    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNameAr() {
        return companyNameAr;
    }

    public void setCompanyNameAr(String companyNameAr) {
        this.companyNameAr = companyNameAr;
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


    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }


    @Override
    public String toString() {
        return "OfferBeanList_New{" +
                "id='" + id + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", categoryNameAr='" + categoryNameAr + '\'' +
                ", status='" + status + '\'' +
                ", countryId='" + countryId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyNameAr='" + companyNameAr + '\'' +
                ", description='" + description + '\'' +
                ", descriptionAr='" + descriptionAr + '\'' +
                ", image='" + image + '\'' +
                ", discountPercentage='" + discountPercentage + '\'' +
                ", discountCode='" + discountCode + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
