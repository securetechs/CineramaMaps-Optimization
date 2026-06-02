package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartBean {
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("before_discount_amount")
    @Expose
    private String beforeDiscountAmount;
    @SerializedName("total_discount_amount")
    @Expose
    private String totalDiscountAmount;
    @SerializedName("total_cart")
    @Expose
    private String totalCart;
    @SerializedName("delivery_fee")
    @Expose
    private String deliveryFee;
    @SerializedName("result")
    @Expose
    private List<CartBeanList> result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBeforeDiscountAmount() {
        return beforeDiscountAmount;
    }

    public void setBeforeDiscountAmount(String beforeDiscountAmount) {
        this.beforeDiscountAmount = beforeDiscountAmount;
    }

    public String getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(String totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public String getTotalCart() {
        return totalCart;
    }

    public void setTotalCart(String totalCart) {
        this.totalCart = totalCart;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(String deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public List<CartBeanList> getResult() {
        return result;
    }

    public void setResult(List<CartBeanList> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
