package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopUpBean {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("isSelected")
    @Expose
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
