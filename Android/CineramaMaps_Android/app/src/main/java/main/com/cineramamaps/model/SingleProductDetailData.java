package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SingleProductDetailData {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("provider_id")
    @Expose
    private String providerId;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("item_price")
    @Expose
    private String itemPrice;
    @SerializedName("offer_item_price")
    @Expose
    private String offerItemPrice;
    @SerializedName("item_description")
    @Expose
    private String itemDescription;
    @SerializedName("item_quantity")
    @Expose
    private String itemQuantity;
    @SerializedName("bag_size_id")
    @Expose
    private String bagSizeId;
    @SerializedName("bag_size_name")
    @Expose
    private String bagSizeName;
    @SerializedName("bag_size_price")
    @Expose
    private String bagSizePrice;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("like_status")
    @Expose
    private String likeStatus;


    public String getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(String likeStatus) {
        this.likeStatus = likeStatus;
    }

    @SerializedName("available_status")
    @Expose
    private String availableStatus;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("remove_status")
    @Expose
    private String removeStatus;
    @SerializedName("product_additional")
    @Expose
    private List<ProductAdditional> productAdditional;
    @SerializedName("product_images")
    @Expose
    private List<ProductImage> productImages;
    @SerializedName("rest_details")
    @Expose
    private RestDetails restDetails;
    @SerializedName("distance")
    @Expose
    private String distance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getOfferItemPrice() {
        return offerItemPrice;
    }

    public void setOfferItemPrice(String offerItemPrice) {
        this.offerItemPrice = offerItemPrice;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getBagSizeId() {
        return bagSizeId;
    }

    public void setBagSizeId(String bagSizeId) {
        this.bagSizeId = bagSizeId;
    }

    public String getBagSizeName() {
        return bagSizeName;
    }

    public void setBagSizeName(String bagSizeName) {
        this.bagSizeName = bagSizeName;
    }

    public String getBagSizePrice() {
        return bagSizePrice;
    }

    public void setBagSizePrice(String bagSizePrice) {
        this.bagSizePrice = bagSizePrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAvailableStatus() {
        return availableStatus;
    }

    public void setAvailableStatus(String availableStatus) {
        this.availableStatus = availableStatus;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(String removeStatus) {
        this.removeStatus = removeStatus;
    }

    public List<ProductAdditional> getProductAdditional() {
        return productAdditional;
    }

    public void setProductAdditional(List<ProductAdditional> productAdditional) {
        this.productAdditional = productAdditional;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public RestDetails getRestDetails() {
        return restDetails;
    }

    public void setRestDetails(RestDetails restDetails) {
        this.restDetails = restDetails;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

}
