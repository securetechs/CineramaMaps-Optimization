package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartDetail {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("company_id")
    @Expose
    private String companyId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String product_name;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("size_id")
    @Expose
    private String sizeId;
    @SerializedName("size_name")
    @Expose
    private String sizeName;
    @SerializedName("size_price")
    @Expose
    private String sizePrice;
    @SerializedName("extra_item_id")
    @Expose
    private String extraItemId;
    @SerializedName("extra_item_qty")
    @Expose
    private String extra_item_qty;
    @SerializedName("extra_item_name")
    @Expose
    private String extraItemName;
    @SerializedName("extra_item_price")
    @Expose
    private String extraItemPrice;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("date_time")
    @Expose
    private String dateTime;

    public String getExtra_item_qty() {
        return extra_item_qty;
    }

    public void setExtra_item_qty(String extra_item_qty) {
        this.extra_item_qty = extra_item_qty;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getSizePrice() {
        return sizePrice;
    }

    public void setSizePrice(String sizePrice) {
        this.sizePrice = sizePrice;
    }

    public String getExtraItemId() {
        return extraItemId;
    }

    public void setExtraItemId(String extraItemId) {
        this.extraItemId = extraItemId;
    }

    public String getExtraItemName() {
        if (extraItemName == null) {
            extraItemName = "";
        }
        return extraItemName;
    }

    public void setExtraItemName(String extraItemName) {
        this.extraItemName = extraItemName;
    }

    public String getExtraItemPrice() {
        return extraItemPrice;
    }

    public void setExtraItemPrice(String extraItemPrice) {
        this.extraItemPrice = extraItemPrice;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
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
