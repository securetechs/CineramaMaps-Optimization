package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProviderBeanList {
    @SerializedName("avg_rating")
    @Expose
    private String avg_rating;
    @SerializedName("today_open_time")
    @Expose
    private String today_open_time;

    @SerializedName("total_rating_count")
    @Expose
    private String total_rating_count;

    @SerializedName("distance")
    @Expose
    private String distance;

    @SerializedName("item_left_quantity")
    @Expose
    private String item_left_quantity;

    @SerializedName("magic_food_left_quantity")
    @Expose
    private String magic_food_left_quantity;
    @SerializedName("item_quantity")
    @Expose
    private String itemQuantity;
    @SerializedName("fav_provider")
    @Expose
    private String fav_provider;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("mobile_with_code")
    @Expose
    private String mobileWithCode;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("state_name")
    @Expose
    private String stateName;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("social_id")
    @Expose
    private String socialId;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("address_id")
    @Expose
    private String addressId;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("wallet")
    @Expose
    private String wallet;
    @SerializedName("register_id")
    @Expose
    private String registerId;
    @SerializedName("ios_register_id")
    @Expose
    private String iosRegisterId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("approve_status")
    @Expose
    private String approveStatus;
    @SerializedName("available_status")
    @Expose
    private String availableStatus;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("store_logo")
    @Expose
    private String storeLogo;
    @SerializedName("store_cover_image")
    @Expose
    private String storeCoverImage;
    @SerializedName("about_store")
    @Expose
    private String aboutStore;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("note_block")
    @Expose
    private String noteBlock;
    @SerializedName("block_unblock")
    @Expose
    private String blockUnblock;
    @SerializedName("remove_status")
    @Expose
    private String removeStatus;
    @SerializedName("open_time")
    @Expose
    private String openTime;
    @SerializedName("close_time")
    @Expose
    private String closeTime;
    @SerializedName("store_ope_closs_status")
    @Expose
    private String storeOpeClossStatus;
    @SerializedName("referral_code")
    @Expose
    private String referralCode;
    @SerializedName("signup_referral_code")
    @Expose
    private String signupReferralCode;
    @SerializedName("provider_name")
    @Expose
    private String providerName;
    @SerializedName("provider_email")
    @Expose
    private String providerEmail;
    @SerializedName("provider_mobile")
    @Expose
    private String providerMobile;
    @SerializedName("provider_streat_address")
    @Expose
    private String providerStreatAddress;
    @SerializedName("provider_lat")
    @Expose
    private String providerLat;
    @SerializedName("provider_lon")
    @Expose
    private String providerLon;
    @SerializedName("provider_post_code")
    @Expose
    private String providerPostCode;
    @SerializedName("provider_country")
    @Expose
    private String providerCountry;
    @SerializedName("provider_country_id")
    @Expose
    private String providerCountryId;
    @SerializedName("facebook_url")
    @Expose
    private String facebookUrl;
    @SerializedName("instagram_url")
    @Expose
    private String instagramUrl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("delivery_option")
    @Expose
    private String deliveryOption;
    @SerializedName("provider_logo")
    @Expose
    private String providerLogo;
    @SerializedName("radius")
    @Expose
    private String radius;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("cat_name")
    @Expose
    private String catName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToday_open_time() {
        return today_open_time;
    }

    public void setToday_open_time(String today_open_time) {
        this.today_open_time = today_open_time;
    }


    public String getTotal_rating_count() {
        return total_rating_count;
    }

    public void setTotal_rating_count(String total_rating_count) {
        this.total_rating_count = total_rating_count;
    }


    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }


    public String getMagic_food_left_quantity() {
        return magic_food_left_quantity;
    }

    public void setMagic_food_left_quantity(String magic_food_left_quantity) {
        this.magic_food_left_quantity = magic_food_left_quantity;
    }

    public String getItem_left_quantity() {
        return item_left_quantity;
    }

    public void setItem_left_quantity(String item_left_quantity) {
        this.item_left_quantity = item_left_quantity;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getFav_provider() {
        return fav_provider;
    }

    public void setFav_provider(String fav_provider) {
        this.fav_provider = fav_provider;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobileWithCode() {
        return mobileWithCode;
    }

    public void setMobileWithCode(String mobileWithCode) {
        this.mobileWithCode = mobileWithCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    public String getIosRegisterId() {
        return iosRegisterId;
    }

    public void setIosRegisterId(String iosRegisterId) {
        this.iosRegisterId = iosRegisterId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getAvailableStatus() {
        return availableStatus;
    }

    public void setAvailableStatus(String availableStatus) {
        this.availableStatus = availableStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public String getStoreCoverImage() {
        return storeCoverImage;
    }

    public void setStoreCoverImage(String storeCoverImage) {
        this.storeCoverImage = storeCoverImage;
    }

    public String getAboutStore() {
        return aboutStore;
    }

    public void setAboutStore(String aboutStore) {
        this.aboutStore = aboutStore;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNoteBlock() {
        return noteBlock;
    }

    public void setNoteBlock(String noteBlock) {
        this.noteBlock = noteBlock;
    }

    public String getBlockUnblock() {
        return blockUnblock;
    }

    public void setBlockUnblock(String blockUnblock) {
        this.blockUnblock = blockUnblock;
    }

    public String getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(String removeStatus) {
        this.removeStatus = removeStatus;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getStoreOpeClossStatus() {
        return storeOpeClossStatus;
    }

    public void setStoreOpeClossStatus(String storeOpeClossStatus) {
        this.storeOpeClossStatus = storeOpeClossStatus;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getSignupReferralCode() {
        return signupReferralCode;
    }

    public void setSignupReferralCode(String signupReferralCode) {
        this.signupReferralCode = signupReferralCode;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderEmail() {
        return providerEmail;
    }

    public void setProviderEmail(String providerEmail) {
        this.providerEmail = providerEmail;
    }

    public String getProviderMobile() {
        return providerMobile;
    }

    public void setProviderMobile(String providerMobile) {
        this.providerMobile = providerMobile;
    }

    public String getProviderStreatAddress() {
        return providerStreatAddress;
    }

    public void setProviderStreatAddress(String providerStreatAddress) {
        this.providerStreatAddress = providerStreatAddress;
    }

    public String getProviderLat() {
        return providerLat;
    }

    public void setProviderLat(String providerLat) {
        this.providerLat = providerLat;
    }

    public String getProviderLon() {
        return providerLon;
    }

    public void setProviderLon(String providerLon) {
        this.providerLon = providerLon;
    }

    public String getProviderPostCode() {
        return providerPostCode;
    }

    public void setProviderPostCode(String providerPostCode) {
        this.providerPostCode = providerPostCode;
    }

    public String getProviderCountry() {
        return providerCountry;
    }

    public void setProviderCountry(String providerCountry) {
        this.providerCountry = providerCountry;
    }

    public String getProviderCountryId() {
        return providerCountryId;
    }

    public void setProviderCountryId(String providerCountryId) {
        this.providerCountryId = providerCountryId;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(String deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    public String getProviderLogo() {
        return providerLogo;
    }

    public void setProviderLogo(String providerLogo) {
        this.providerLogo = providerLogo;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
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


}
