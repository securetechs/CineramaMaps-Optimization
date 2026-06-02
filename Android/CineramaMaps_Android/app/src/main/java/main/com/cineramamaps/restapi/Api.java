package main.com.cineramamaps.restapi;


import java.util.HashMap;

import main.com.cineramamaps.model.CategoryResponse;
import main.com.cineramamaps.model.ChildCategoryResponse;
import main.com.cineramamaps.model.CityResponse;
import main.com.cineramamaps.model.GuidelineCategoryResponse;
import main.com.cineramamaps.model.GuidelineChildSubCategoryResponse;
import main.com.cineramamaps.model.GuidelineSubCategoryResponse;
import main.com.cineramamaps.model.Guidelines_child_Response;
import main.com.cineramamaps.model.Guild_cat_response;
import main.com.cineramamaps.model.Guildline_subcat_response;
import main.com.cineramamaps.model.IPCountryCheckModel;
import main.com.cineramamaps.model.PartnerServiceCategoryResponse;
import main.com.cineramamaps.model.PartnerServiceChildSubCategoryResponse;
import main.com.cineramamaps.model.PartnerServiceSubCategoryResponse;
import main.com.cineramamaps.model.ProfileResponse;
import main.com.cineramamaps.model.SubCategoryResponse;
import main.com.cineramamaps.model.TourismCatResponse;
import main.com.cineramamaps.model.TourismChildSubCatResponse;
import main.com.cineramamaps.model.TourismSubCatResponse;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface Api {
    @GET("get_conversation_detail?")
    Call<ResponseBody> getBuyConversationList(@Query("receiver_id") String receiver_id);

    @GET("add_user_trip_schedule")
    Call<ResponseBody> Send_Trip(@QueryMap HashMap<String, String> map);

    @GET("update_user_trip_schedule")
    Call<ResponseBody> Update_Trip(@QueryMap HashMap<String, String> map);

    @GET("send_otp")
    Call<ResponseBody> Send_Otp(@QueryMap HashMap<String, String> map);

    @GET("send_otp_mail")
    Call<ResponseBody> Send_Otp_Email(@QueryMap HashMap<String, String> map);

    @GET("login")
    Call<ResponseBody> login(@QueryMap HashMap<String, String> map);

/*    @GET("signup")
    Call<ResponseBody> signup(@QueryMap HashMap<String, String> map);*/


    @GET("verify_number?")
    Call<ResponseBody> verifyOtp(@QueryMap HashMap<String, String> map);

    @GET("add_to_cart_product?")
    Call<ResponseBody> addToCart(@QueryMap HashMap<String, String> map);

    @GET("like_unlike_product?")
    Call<ResponseBody> addTofav(@QueryMap HashMap<String, String> map);

    @GET("fav_unfav_citymap?")
    Call<ResponseBody> addTofav1(@QueryMap HashMap<String, String> map);

    @GET("fav_unfav_place?")
    Call<ResponseBody> addTolike(@QueryMap HashMap<String, String> map);

    @GET("get_cart?")
    Call<ResponseBody> getMyCart(@Query("user_id") String user_id);

    @GET("delete_cart_item?")
    Call<ResponseBody> deleteCart(@Query("cart_id") String cart_id);

    @GET("update_cart?")
    Call<ResponseBody> updateCart(@Query("cart_id") String cart_id, @Query("type") String type, @Query("quantity") String quantity);

    //http://35.183.250.236/CineramaMaps/webservice/add_rating_review_by_order
    @GET("add_city_rating_review?")
    Call<ResponseBody> giveRating(@QueryMap HashMap<String, String> map);

    @GET("add_service_rating_review?")
    Call<ResponseBody> giveRating1(@QueryMap HashMap<String, String> map);

    @GET("update_location?")
    Call<ResponseBody> updateAddress(@QueryMap HashMap<String, String> map);

    @GET("place_order?")
    Call<ResponseBody> placeOrder(@QueryMap HashMap<String, String> map);

    //http://35.183.250.236/CineramaMaps/webservice/get_user_order_by_status?user_id=50&type=Current
    @GET("get_user_order_by_status?")
    Call<ResponseBody> getUserOrders(@QueryMap HashMap<String, String> map);


    @GET("get_category?")
    Call<ResponseBody> getCategory(@Query("user_id") String userID);

    @GET("get_country_map_by_user?")
    Call<ResponseBody> getMapType(@Query("user_id") String userID);

    @GET("get_user_trip_schedule_details?")
    Call<ResponseBody> getTripDetails(@Query("user_id") String user_id, @Query("trip_id") String trip_id);

    @GET("get_user_page?")
    Call<ResponseBody> getPageTerm();

    //http://35.183.250.236/CineramaMaps/webservice/get_provider_review_rating?&provider_id=48
    @GET("get_provider_review_rating?")
    Call<ResponseBody> getProviderRating(@Query("provider_id") String provider_id);

    @GET("get_user_trip_schedule?")
    Call<ResponseBody> getUserTrip(@Query("user_id") String user_id);

    @GET("get_user_trip_schedule_by_day?")
    Call<ResponseBody> getPlaceTrip(@Query("user_id") String user_id,
                                    @Query("city_id") String city_id,
                                    @Query("lat") String lat,
                                    @Query("lon") String lon,
                                    @Query("table_map_name") String table_map_name);

    @GET("get_notification_list?")
    Call<ResponseBody> getMyNotifications(@Query("user_id") String user_id);

    @GET("get_plans?")
    Call<ResponseBody> getPlan(@Query("user_id") String user_id);

    @GET("get_faq?")
    Call<ResponseBody> getMyFAQ(@Query("user_id") String user_id);


    //http://35.183.250.236/CineramaMaps/webservice/get_transaction?user_id=22
    @GET("get_transaction?")
    Call<ResponseBody> getTransactions(@Query("user_id") String user_id);


    //https://shly.fr/shly/webservice/get_banner_image
    @GET("get_banner_image?")
    Call<ResponseBody> getBannerImage(@Query("user_id") String userID);

    //https://techimmense.in/Shif/webservice/get_request_details?&request_id=1
    @GET("get_request_details?")
    Call<ResponseBody> getBookingDetails(@Query("user_id") String userID, @Query("request_id") String request_id);


    @GET("get_sub_category_list?")
    Call<ResponseBody> getSubCategory(@Query("user_id") String userID, @Query("category_id") String category_id);

    @GET("get_provider_service_by_category?")
    Call<ResponseBody> getServiceByCategory(@Query("user_id") String user_id, @Query("provider_id") String provider_id);


    @GET("get_general_activity_category?")
    Call<ResponseBody> getActivityCategory(@Query("user_id") String userID);

    @Multipart
    @POST("update_profile")
    Call<ProfileResponse> updateProfile(@QueryMap HashMap<String, String> map, @Part MultipartBody.Part filePart);

    @Multipart
    @POST("signup")
    Call<ProfileResponse> createProfile(@QueryMap HashMap<String, String> map, @Part MultipartBody.Part filePart);

        @GET("get_services?")
    Call<ResponseBody> getServices(@QueryMap HashMap<String, String> map);
    @GET("get_partner_services?")
    Call<ResponseBody> getPartnerServices(@QueryMap HashMap<String, String> map);

    @GET("get_tourism_services?")
    Call<ResponseBody> getTourismServices(@QueryMap HashMap<String, String> map);

    @Multipart
    @POST("signup")
    Call<ProfileResponse> signup(@QueryMap HashMap<String, String> map, @Part MultipartBody.Part filePart);

    @Multipart
    @POST("social_login")
    Call<ProfileResponse> getSociallogin(@QueryMap HashMap<String, String> map, @Part MultipartBody.Part filePart);


    @GET("get_banner?")
    Call<ResponseBody> get_Banner(@QueryMap HashMap<String, String> map);

    @GET("get_product_list_by_filter?")
    Call<ResponseBody> getMagicFoods(@QueryMap HashMap<String, String> map);

    //http://35.183.250.236/CineramaMaps/webservice/get_product_list_by_filter?cat_id
    @GET("get_product_list_by_filter?")
    Call<ResponseBody> getDiscountFoods(@QueryMap HashMap<String, String> map);

    @GET("get_my_fav_product?")
    Call<ResponseBody> getFavProducts(@QueryMap HashMap<String, String> map);

//    @GET("get_company_offer?")
//    Call<ResponseBody> getCompanyOffer(@QueryMap HashMap<String, String> map);

    @GET("get_my_fav_provider?")
    Call<ResponseBody> getFavRestaurant(@QueryMap HashMap<String, String> map);

    @GET("get_country_map_city?")
    Call<ResponseBody> getCityMaps(@QueryMap HashMap<String, String> map);

    @GET("get_purcahse_city_map_list?")
    Call<ResponseBody> getCityMaps1(@QueryMap HashMap<String, String> map);

    @GET("get_days?")
    Call<ResponseBody> getDay(@QueryMap HashMap<String, String> map);

    @GET("get_user_trip_schedule_map_name?")
    Call<ResponseBody> getMapName(@QueryMap HashMap<String, String> map);

    @GET("my_fav_citymap?")
    Call<ResponseBody> myfav_citymap(@QueryMap HashMap<String, String> map);

    // @GET("get_country_map_city_places?")
    @GET("get_country_map_city_places_new?")
    Call<ResponseBody> getCityMapsDetails(@QueryMap HashMap<String, String> map);

    @GET("get_country_map_city_images?")
    Call<ResponseBody> getCityImages(@QueryMap HashMap<String, String> map);

    @GET("get_plans?")
    Call<ResponseBody> getPlan(@QueryMap HashMap<String, String> map);


    @GET("add_wallet_amount?")
    Call<ResponseBody> addTopUpinWallet(@QueryMap HashMap<String, String> map);

    @GET("plan_purchase?")
    Call<ResponseBody> addPayment(@QueryMap HashMap<String, String> map);

    @GET("addPayment_moyasar?")
    Call<ResponseBody> checkPayment(@QueryMap HashMap<String, String> map);

    @GET("verifyPayment_moyasar?")
    Call<ResponseBody> verifyPayment(@QueryMap HashMap<String, String> map);


    //http://35.183.250.236/CineramaMaps/webservice/get_product_details?product_id=1
    @GET("get_service_details?")
    Call<ResponseBody> getItemDetail(@Query("user_id") String userID,
                                     @Query("service_id") String service_id,
                                     @Query("lat") String lat,
                                     @Query("lon") String lon);

    @GET("get_place_details?")
    Call<ResponseBody> getPlaceDetail(@Query("user_id") String userID,
                                      @Query("place_id") String place_id,
                                      @Query("lat") String lat,
                                      @Query("lon") String lon);

    @GET("get_details_by_place_id_googlemap?")
    Call<ResponseBody> getPlaceDetail1(@Query("user_id") String userID,
                                       @Query("id") String id,
                                       @Query("place_id") String place_id,
                                       @Query("lat") String lat,
                                       @Query("lon") String lon,
                                       @Query("lang") String lang);


    @GET("get_country_map?")
    Call<ResponseBody> getMaplist(@Query("user_id") String userID,
                                  @Query("token") String token,
                                  @Query("type") String type);

    @GET("get_guidelines_tips?")
    Call<ResponseBody> getGuidelinelist(@Query("user_id") String userID,
                                        @Query("city_id") String city_id,
                                        @Query("country_id") String country_id,
                                        @Query("token") String token);

    //getRestaurants
    @GET("get_all_rest_list?")
    Call<ResponseBody> getRestaurants(@Query("user_id") String userID, @Query("token") String token,
                                      @Query("cat_id") String cat_id,
                                      @Query("lat") String lat,
                                      @Query("lon") String lon,
                                      @Query("diet_type_vegan") String diet_type_vegan,
                                      @Query("day_name") String day_name,
                                      @Query("start_time") String start_time,
                                      @Query("end_time") String end_time);


    @GET("get_conversation_detail?")
    Call<ResponseBody> getMyConversatiosn(@Query("receiver_id") String receiver_id);

    @GET("delete_user_account?")
    Call<ResponseBody> deleteUserAccount(@Query("user_id") String user_id);

    @GET("add_user_address?")
    Call<ResponseBody> addAddress(@Query("user_id") String user_id, @Query("addresstype") String title, @Query("address") String address, @Query("lat") String lat, @Query("lon") String lon, @Query("timezone") String timezone);

    @GET("get_user_address?")
    Call<ResponseBody> getMyAddress(@Query("user_id") String user_id);

    @GET("delete_user_address?")
    Call<ResponseBody> deleteaddress(@Query("address_id") String address_id);

    //https://techimmense.in/Shif/webservice/get_filter_provider
    @GET("get_filter_provider?")
    Call<ResponseBody> getFilterProvider(@Query("lat") String lat, @Query("lon") String lon, @Query("cat_id") String cat_id, @Query("sub_cat_id") String sub_cat_id);


    //add_store_employee

    @GET("change_password?")
    Call<ResponseBody> changepassword(
            @Query("user_id") String user_id,
            @Query("password") String password,
            @Query("old_password") String old_password);

    @GET("forgot_password?")
    Call<ResponseBody> ForgotCall(@Query("email") String email);

    @GET("get_coupons?")
    Call<ResponseBody> getCoupons1(@Query("user_id") String user_id);

    @GET("apply_offer?")
    Call<ResponseBody> applyCoupon(
            @Query("offer_code") String offer_code,
            @Query("user_id") String user_id,
            @Query("amount") String amount);

    @GET("send_feedback?")
    Call<ResponseBody> sendFeedback(
            @Query("user_id") String user_id,
            @Query("email") String email,
            @Query("name") String name,
            @Query("contact_number") String contact_number,
            @Query("feedback") String feedback);

    @GET("add_country_map_suggestion?")
    Call<ResponseBody> sendSuggestion(
            @Query("user_id") String user_id,
            @Query("country_name") String country_name,
            @Query("description") String description);


    //https://techimmense.in/FMWB/webservice/add_feed_post?user_id=&post_type=&timezone=&description=
    @GET("add_feed_post?")
    Call<ResponseBody> addFeedSimple(@Query("user_id") String user_id, @Query("description") String description, @Query("post_type") String post_type, @Query("timezone") String timezone);

    //  @POST("webservice/get_user_address")
    @POST("get_user_address")
    Call<ResponseBody> get_user_address(
            @Query("user_id") String user_id);

    @POST("add_user_address")
    Call<ResponseBody> add_user_address(
            @Query("user_id") String user_id,
            @Query("address") String address,
            @Query("villa_name") String villa_name,
            @Query("villa_no") String villa_no,
            @Query("postal_code") String postal_code,
            @Query("country") String country,
            @Query("contact_number") String contact_number,
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("cost") String cost,
            @Query("rentstatus") String rentstatus,
            @Query("addresstype") String addresstype);

    @POST("delete_user_address")
    Call<ResponseBody> delete_user_address(
            @Query("address_id") String address_id);

    @POST("addTofavorite")
    Call<ResponseBody> addTofavorite(
            @Query("user_id") String user_id,
            @Query("place_id") String place_id);

    @POST("delete_user_trip_schedule")
    Call<ResponseBody> delete_trip(
            @Query("trip_id") String trip_id);

    @POST("delete_user_trip_schedule_by_city")
    Call<ResponseBody> delete_trip_city(
            @Query("user_id") String user_id,
            @Query("city_id") String city_id);

    @GET("get_profile?")
    Call<ResponseBody> getProfile(@Query("user_id") String userID);


    @GET("cancel_plan_purchase?")
    Call<ResponseBody> getRemovePlan(@Query("user_id") String user_id,
                                     @Query("map_country_id") String map_country_id,
                                     @Query("map_city_id") String map_city_id);


    @GET("get_photos_by_place_id_googlemap?")
    Call<ResponseBody> getPhoto(@Query("place_id") String place_id,
                                @Query("id") String id,
                                @Query("lang") String lang);

    @GET("get_place_id_by_addresss_googlemap?")
    Call<ResponseBody> getPlaceid(
            @Query("address") String address,
            @Query("db_place_id") String db_place_id,
            @Query("lang") String lang);

    @GET("check_plan_purchase_city_wise?")
    Call<ResponseBody> getPurchaseDetails(@Query("user_id") String user_id, @Query("map_city_id") String map_city_id);

    @GET("get_provider_details?")
    Call<ResponseBody> getSaloonBarberDetail(@Query("provider_id") String provider_id);

    @GET("get_bannner_list?")
    Call<ResponseBody> getBanner(@Query("user_id") String userID);

    @GET("stripe_payment?")
        //@GET("strip_payment_new?")
    Call<ResponseBody> stripeChargeApi(@Query("user_id") String user_id, @Query("provider_id") String provider_id, @Query("total_amount") String total_amount, @Query("payment_method") String payment_method, @Query("token") String token, @Query("currency") String currency, @Query("request_id") String request_id);


    @GET("save_card_stripe?")
    Call<ResponseBody> saveCard(@Query("user_id") String user_id, @Query("customer_id") String customer_id, @Query("tok_visa") String tok_visa);

    @GET("retrieve_all_card_stripe?")
    Call<ResponseBody> getMySavedCards(@Query("user_id") String userID, @Query("customer_id") String customer_id);


    @GET("delete_saved_card?")
    Call<ResponseBody> deleteSavedCards(@Query("user_id") String userID, @Query("customer_id") String customer_id, @Query("card_id") String card_id);


    @Multipart
    @POST("update_provider_profile")
    Call<ResponseBody> updateBarberProfile(
            @Query("user_id") String user_id,
            @Query("store_name") String store_name,
            @Query("about_store") String about_store,
            @Query("address") String address,
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("store_ope_closs_status") String store_ope_closs_status,
            @Query("open_day") String open_day,
            @Query("open_time") String open_time,
            @Query("close_time") String close_time,
            @Query("home_service") String home_service,
            @Query("allow_reward_point") String allow_reward_point,
            @Query("reward_point") String reward_point,
            @Query("reward_point_value") String reward_point_value,
            @Query("home_service_fee") String home_service_fee,
            @Query("interval_time") String interval_time,
            @Part MultipartBody.Part file,
            @Part MultipartBody.Part filef

    );

    @GET("update_location?")
    Call<ResponseBody> updateProviderLocation(@Query("user_id") String user_id, @Query("lat") String lat, @Query("lon") String lon);


    @GET("add_service?")
    Call<ResponseBody> addServices(@QueryMap HashMap<String, String> map);

    @GET("get_provider_service?")
    Call<ResponseBody> getMyServices(@Query("user_id") String user_id);


    @GET("provider_delete_service?")
    Call<ResponseBody> deleteServices(@Query("service_id") String service_id);


    @GET("provider_update_service?")
    Call<ResponseBody> updateService(@QueryMap HashMap<String, String> map);


    @GET("get_provider_image_list?")
    Call<ResponseBody> getGallaryImages(@Query("user_id") String user_id);

    @GET("delete_provider_image_new?")
    Call<ResponseBody> deleteGallaryImage(@Query("provider_image_id") String provider_image_id);


    //delete_offer?coupon_id
    @GET("delete_offer?")
    Call<ResponseBody> deleteOfferCoupon(@Query("coupon_id") String coupon_id);


    @Multipart
    @POST("add_offer")
    Call<ResponseBody> addOffer(@QueryMap HashMap<String, String> map, @Part MultipartBody.Part filePart);


    @GET("get_provider_details?")
    Call<ResponseBody> saloonDetail(@Query("user_id") String user_id, @Query("provider_id") String provider_id);


    @GET("get_offer?")
    Call<ResponseBody> getCoupons(@Query("user_id") String user_id);

    @GET("get_this_saloon_offer?")
    Call<ResponseBody> getSelBarberCoupons(@Query("user_id") String user_id, @Query("provider_id") String provider_id);


    @GET("add_book_appointment?")
    Call<ResponseBody> addBooking(@QueryMap HashMap<String, String> map);

    @GET("apply_offer?")
    Call<ResponseBody> applyOffer(@QueryMap HashMap<String, String> map);


    @GET("apply_reward_points?")
    Call<ResponseBody> applyRewards(@QueryMap HashMap<String, String> map);

    @GET("get_time_slot?")
    Call<ResponseBody> getAvbTimeSlot(@Query("user_id") String user_id, @Query("saloon_id") String saloon_id, @Query("now_current_day") String now_current_day, @Query("current_date") String current_date);

    @GET("check_time_slot?")
    Call<ResponseBody> getAvbProviderTimeSlot(@Query("user_id") String user_id, @Query("barber_id") String barber_id, @Query("now_current_day") String now_current_day, @Query("current_date") String current_date);

    @GET("get_user_book_appointment_list?")
    Call<ResponseBody> getUserBooking(@Query("user_id") String user_id, @Query("status") String status);


    @GET("get_user_page?")
    Call<ResponseBody> getUserPage();

    @GET("get_blog_covid_link?")
    Call<ResponseBody> getBlogandLinks();

    @GET("get_provider_total_earning?")
    Call<ResponseBody> getProviderEarning(@Query("provider_id") String provider_id);


    @GET("get_proivder_book_appointment?")
    Call<ResponseBody> getSaloonBooking(@Query("provider_id") String provider_id, @Query("status") String status, @Query("date") String date);

    @GET("change_request_status?")
    Call<ResponseBody> changeStatusofBooking(@Query("request_id") String request_id, @Query("provider_id") String provider_id, @Query("user_id") String user_id, @Query("status") String status, @Query("reason_title") String reason_title, @Query("reason_detail") String reason_detail, @Query("cancelation_fee") String cancelation_fee);


    @GET("add_rating_review?")
    Call<ResponseBody> giveRating(@Query("request_id") String request_id, @Query("form_id") String form_id, @Query("to_id") String to_id, @Query("rating") String rating, @Query("feedback") String feedback, @Query("type") String type);

    @GET("add_rating_review?")
    Call<ResponseBody> giveRatingByProvider(@Query("request_id") String request_id, @Query("form_id") String form_id, @Query("to_id") String to_id, @Query("rating") String rating, @Query("feedback") String feedback, @Query("type") String type);

    @GET("get_rating_review?")
    Call<ResponseBody> getRatingReview(@Query("user_id") String user_id);

    @GET("fav_unfav?")
    Call<ResponseBody> setFavUnfav(@Query("user_id") String user_id, @Query("provider_id") String provider_id, @Query("type") String type);


    @GET("barber_saloon_offer_list?")
    Call<ResponseBody> getSaloonsCoupons(@Query("user_id") String user_id);

    @GET("get_user_total_reward_list?")
    Call<ResponseBody> getRewards(@Query("user_id") String user_id);


    //social_login?
    @GET("social_login?")
    Call<ResponseBody> socialLogin(@Query("social_id") String social_id, @Query("first_name") String first_name, @Query("last_name") String last_name, @Query("image") String image, @Query("email") String email, @Query("register_id") String register_id, @Query("ios_register_id") String ios_register_id, @Query("lat") String lat, @Query("lon") String lon, @Query("type") String type);

    @GET("check_cancel_fee?")
    Call<ResponseBody> cancelFeeCheck(@Query("request_id") String request_id, @Query("current_date") String current_date, @Query("timezone") String timezone);


    @GET("get_user_notification_list?")
    Call<ResponseBody> getNotifications(@Query("user_id") String user_id);


    @GET("add_withdraw_request?")
    Call<ResponseBody> sendWithdrawRequest(@Query("user_id") String user_id, @Query("amount") String amount, @Query("account_number") String account_number, @Query("account_holder_name") String account_holder_name, @Query("ifsc_code") String ifsc_code, @Query("description") String description);

    @GET("get_withdraw_request?")
    Call<ResponseBody> getPendingWithdrawRequest(@Query("user_id") String user_id);


    @GET("get_profile?")
    Call<ResponseBody> getActivepaymetntye(@Query("user_id") String user_id);

    @GET
    Call<ResponseBody> getExchange(@Url String url);

    @POST
    Call<ResponseBody> openPayTab(@Url String url, @Query("first_name") String first_name,
                                  @Query("last_name") String last_name, @Query("email") String email,
                                  @Query("number") String number, @Query("order_id") String order_id,
                                  @Query("amount") String amount, @Query("currency") String currency
    );


    @GET("get_company_offer_new?")
    Call<ResponseBody> getcompanyOffernew(@QueryMap HashMap<String, String> map);

    @GET("get_offer_category")
    Call<CategoryResponse> getOfferCategories(@Query("user_id") int userId);

    @GET("get_offer_sub_category")
    Call<SubCategoryResponse> getOfferSubCategories(
            @Query("user_id") String userId,
            @Query("cat_id") String catId
    );

    @GET("get_offer_child_sub_category")
    Call<ChildCategoryResponse> getOfferChildCategories(
            @Query("user_id") int userId,
            @Query("cat_id") String catId,
            @Query("sub_cat_id") String subCatId
    );


    @GET("get_tourism_services_category")
    Call<TourismCatResponse> getTourismoffercat(@Query("user_id") int userId);

    @GET("get_tourism_services_sub_category")
    Call<TourismSubCatResponse> getTourismSubCategories(
            @Query("cat_id") String categoryId,
            @Query("user-id") String userId
    );

    @GET("get_tourism_services_child_sub_category")
    Call<TourismChildSubCatResponse> getTourismChildSubCategories(
            @Query("cat_id") String catId,
            @Query("sub_cat_id") String subCatId,
            @Query("user-id") int userId
    );

    @GET("get_partner_services_category")
    Call<PartnerServiceCategoryResponse> getPartnercat(@Query("user_id") int userId);

    @GET("get_partner_services_sub_category")
    Call<PartnerServiceSubCategoryResponse> getPartnerSubCategories(
            @Query("cat_id") String categoryId,
            @Query("user-id") String userId
    );

    @GET("get_partner_services_child_sub_category")
    Call<PartnerServiceChildSubCategoryResponse> getPartnerChildSubCategories(
            @Query("cat_id") String catId,
            @Query("sub_cat_id") String subCatId,
            @Query("user-id") int userId
    );

//    @GET("get_guidelines_tips_category")
//    Call<Guild_cat_response> getguidcat(@Query("user_id") int userId);

    @GET("get_category_behalf_city")
    Call<Guild_cat_response> getguidcat(@Query("city_id") String cityId, @Query("user_id") int userId);
    @GET("get_guidelines_tips_sub_category")
    Call<Guildline_subcat_response> getguidsubcat(
            @Query("cat_id") String categoryId,
            @Query("user-id") int userId
    );

    @GET("get_guidelines_tips_child_sub_category")
    Call<Guidelines_child_Response> getguidchild(
            @Query("cat_id") String catId,
            @Query("sub_cat_id") String subCatId,
            @Query("user-id") int userId
    );

    @GET("get_guidelines_tips_new?")
    Call<ResponseBody> getGuidelinelistnew(@QueryMap HashMap<String, String> map);

    @GET("get_guidelines_tips_category_NonSubcriber")
    Call<GuidelineCategoryResponse> getguidnscat(@Query("user_id") int userId);

    @GET("get_guidelines_tips_sub_category_NonSubcriber")
    Call<GuidelineSubCategoryResponse> getguidnssubcat(
            @Query("cat_id") String categoryId,
            @Query("user-id") String userId
    );
    @GET("get_guidelines_tips_child_sub_category_NonSubcriber")
    Call<GuidelineChildSubCategoryResponse> getguidnschild(
            @Query("cat_id") String catId,
            @Query("sub_cat_id") String subCatId,
            @Query("user-id") int userId
    );

    @GET("get_guidelines_tips_new_NonSubcriber?")
    Call<ResponseBody> getGuidelinelistnew_ns(@QueryMap HashMap<String, String> map);

    @GET("get_user_subcriber_city?")
    Call<CityResponse> getCities(@Query("user_id") int userId);
}
