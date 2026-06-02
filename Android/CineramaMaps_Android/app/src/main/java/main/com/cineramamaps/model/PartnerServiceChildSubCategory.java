package main.com.cineramamaps.model;

import com.google.gson.annotations.SerializedName;

public class PartnerServiceChildSubCategory {
    @SerializedName("id")
    private String id;

    @SerializedName("category_id")
    private String categoryId;

    @SerializedName("sub_cat_id")
    private String subCatId;

    @SerializedName("name")
    private String name;

    @SerializedName("name_ar")
    private String nameAr;

    @SerializedName("date_time")
    private String dateTime;

    public String getId() {
        return id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public String getName() {
        return name;
    }

    public String getNameAr() {
        return nameAr;
    }

    public String getDateTime() {
        return dateTime;
    }
}
