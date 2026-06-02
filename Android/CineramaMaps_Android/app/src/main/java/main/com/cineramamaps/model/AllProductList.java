package main.com.cineramamaps.model;


import java.util.ArrayList;

public class AllProductList {

    String categoryid;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String image;

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public ArrayList<Menudata> getMenulist() {
        return menulist;
    }

    public void setMenulist(ArrayList<Menudata> menulist) {
        this.menulist = menulist;
    }

    String categoryname;
    ArrayList<Menudata> menulist;
}
