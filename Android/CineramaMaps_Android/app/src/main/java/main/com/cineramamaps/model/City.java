package main.com.cineramamaps.model;

import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("name_ar")
    private String nameAr;

    public City(String id, String name, String nameAr) {
        this.id = id;
        this.name = name;
        this.nameAr = nameAr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getNameAr() {
        return nameAr;
    }
}
