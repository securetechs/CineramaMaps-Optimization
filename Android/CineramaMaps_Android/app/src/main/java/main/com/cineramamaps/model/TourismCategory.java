package main.com.cineramamaps.model;

public class TourismCategory {
    private String id;
    private String name;
    private String name_ar;
    private String price;

    public TourismCategory(String id, String name, String price, String name_ar) {
        this.id = id;
        this.name = name;
        this.name_ar = name_ar;
        this.price = price;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getName_ar() {
        return name_ar;
    }

    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
