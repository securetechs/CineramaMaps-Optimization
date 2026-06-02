package main.com.cineramamaps.model;

public class Chatdata {
    String chatid;
    String carid;
    String lastmessage;

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public String getCarid() {
        return carid;
    }

    public void setCarid(String carid) {
        this.carid = carid;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }

    public String getCarname() {
        return carname;
    }

    public void setCarname(String carname) {
        this.carname = carname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String senderid;
    String sendername;

    public String getSenderimage() {
        return senderimage;
    }

    public void setSenderimage(String senderimage) {
        this.senderimage = senderimage;
    }

    String senderimage;
    String carname;
    String date;
    Integer no_of_message;

    public Integer getNo_of_message() {
        return no_of_message;
    }

    public void setNo_of_message(Integer no_of_message) {
        this.no_of_message = no_of_message;
    }
}
