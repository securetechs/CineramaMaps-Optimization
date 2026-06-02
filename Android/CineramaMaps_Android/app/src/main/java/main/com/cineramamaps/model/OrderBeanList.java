package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderBeanList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("provider_id")
    @Expose
    private String providerId;
    @SerializedName("cart_id")
    @Expose
    private String cartId;
    @SerializedName("address_id")
    @Expose
    private String addressId;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("before_discount_amount")
    @Expose
    private String beforeDiscountAmount;
    @SerializedName("total_discount_amount")
    @Expose
    private String totalDiscountAmount;
    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;
    @SerializedName("delivery_time")
    @Expose
    private String deliveryTime;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("delivery_status")
    @Expose
    private String deliveryStatus;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("accept_driver_id")
    @Expose
    private String acceptDriverId;
    @SerializedName("remove_status")
    @Expose
    private String removeStatus;
    @SerializedName("order_ready_time")
    @Expose
    private String orderReadyTime;
    @SerializedName("delivery_fee")
    @Expose
    private String deliveryFee;
    @SerializedName("sub_total")
    @Expose
    private String subTotal;
    @SerializedName("driver_del_fee")
    @Expose
    private String driverDelFee;
    @SerializedName("admin_com_fee")
    @Expose
    private String adminComFee;
    @SerializedName("delivery_type")
    @Expose
    private String deliveryType;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("offer_id")
    @Expose
    private String offerId;
    @SerializedName("offer_code")
    @Expose
    private String offerCode;
    @SerializedName("cart_details")
    @Expose
    private List<CartDetail> cartDetails;
    @SerializedName("address_details")
    @Expose
    private AddressDetails addressDetails;
    @SerializedName("product_details")
    @Expose
    private List<ItemBeanList> productDetails;
    @SerializedName("rest_details")
    @Expose
    private RestDetails restDetails;
    @SerializedName("rating_review")
    @Expose
    private String ratingReview;

    public String getRatingReview() {
        return ratingReview;
    }

    public void setRatingReview(String ratingReview) {
        this.ratingReview = ratingReview;
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

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

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

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getAcceptDriverId() {
        return acceptDriverId;
    }

    public void setAcceptDriverId(String acceptDriverId) {
        this.acceptDriverId = acceptDriverId;
    }

    public String getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(String removeStatus) {
        this.removeStatus = removeStatus;
    }

    public String getOrderReadyTime() {
        return orderReadyTime;
    }

    public void setOrderReadyTime(String orderReadyTime) {
        this.orderReadyTime = orderReadyTime;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(String deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getDriverDelFee() {
        return driverDelFee;
    }

    public void setDriverDelFee(String driverDelFee) {
        this.driverDelFee = driverDelFee;
    }

    public String getAdminComFee() {
        return adminComFee;
    }

    public void setAdminComFee(String adminComFee) {
        this.adminComFee = adminComFee;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(String offerCode) {
        this.offerCode = offerCode;
    }

    public List<CartDetail> getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(List<CartDetail> cartDetails) {
        this.cartDetails = cartDetails;
    }

    public AddressDetails getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(AddressDetails addressDetails) {
        this.addressDetails = addressDetails;
    }

    public List<ItemBeanList> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<ItemBeanList> productDetails) {
        this.productDetails = productDetails;
    }

    public RestDetails getRestDetails() {
        return restDetails;
    }

    public void setRestDetails(RestDetails restDetails) {
        this.restDetails = restDetails;
    }


}
