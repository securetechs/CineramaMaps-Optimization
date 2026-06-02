package main.com.cineramamaps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationBeanList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("answer")
    @Expose
    private String answer;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("other_user_id")
    @Expose
    private String otherUserId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("title_ar")
    @Expose
    private String title_ar;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("message_ar")
    @Expose
    private String message_ar;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("user_details")
    @Expose
    private UserDetails userDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage_ar() {
        return message_ar;
    }

    public void setMessage_ar(String message_ar) {
        this.message_ar = message_ar;
    }

    public String getTitle_ar() {
        return title_ar;
    }

    public void setTitle_ar(String title_ar) {
        this.title_ar = title_ar;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }


}
