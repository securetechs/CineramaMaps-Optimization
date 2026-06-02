package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartBeanList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("provider_id")
    @Expose
    private String providerId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
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
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("extra_item_id")
    @Expose
    private String extraItemId;
    @SerializedName("extra_item_name")
    @Expose
    private String extraItemName;
    @SerializedName("extra_item_price")
    @Expose
    private String extraItemPrice;
    @SerializedName("extra_item_qty")
    @Expose
    private String extraItemQty;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("total_extra_item_price")
    @Expose
    private String totalExtraItemPrice;
    @SerializedName("before_discount_amount")
    @Expose
    private String beforeDiscountAmount;
    @SerializedName("after_discount_amount")
    @Expose
    private String afterDiscountAmount;
    @SerializedName("discount_amount")
    @Expose
    private String discountAmount;
    @SerializedName("offer_id")
    @Expose
    private String offerId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("offer_apply_status")
    @Expose
    private String offerApplyStatus;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("product_details")
    @Expose
    private SingleProductDetailData productDetails;
    @SerializedName("rest_details")
    @Expose
    private RestDetails restDetails;

    public RestDetails getRestDetails() {
        return restDetails;
    }

    public void setRestDetails(RestDetails restDetails) {
        this.restDetails = restDetails;
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

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
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

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getExtraItemId() {
        return extraItemId;
    }

    public void setExtraItemId(String extraItemId) {
        this.extraItemId = extraItemId;
    }

    public String getExtraItemName() {
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

    public String getExtraItemQty() {
        return extraItemQty;
    }

    public void setExtraItemQty(String extraItemQty) {
        this.extraItemQty = extraItemQty;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalExtraItemPrice() {
        return totalExtraItemPrice;
    }

    public void setTotalExtraItemPrice(String totalExtraItemPrice) {
        this.totalExtraItemPrice = totalExtraItemPrice;
    }

    public String getBeforeDiscountAmount() {
        return beforeDiscountAmount;
    }

    public void setBeforeDiscountAmount(String beforeDiscountAmount) {
        this.beforeDiscountAmount = beforeDiscountAmount;
    }

    public String getAfterDiscountAmount() {
        return afterDiscountAmount;
    }

    public void setAfterDiscountAmount(String afterDiscountAmount) {
        this.afterDiscountAmount = afterDiscountAmount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOfferApplyStatus() {
        return offerApplyStatus;
    }

    public void setOfferApplyStatus(String offerApplyStatus) {
        this.offerApplyStatus = offerApplyStatus;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public SingleProductDetailData getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(SingleProductDetailData productDetails) {
        this.productDetails = productDetails;
    }

}
