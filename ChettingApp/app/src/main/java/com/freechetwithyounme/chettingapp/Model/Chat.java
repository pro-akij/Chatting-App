package com.freechetwithyounme.chettingapp.Model;

public class Chat {
    private String senderID;
    private String receiverID;
    private String message;
    private String chatimage;
    private String type;

    public Chat(String senderID, String receiverID, String message, String chatimage, String type) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.message = message;
        this.chatimage = chatimage;
        this.type = type;
    }

    public Chat() {
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatimage() {
        return chatimage;
    }

    public void setChatimage(String chatimage) {
        this.chatimage = chatimage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
