package com.freechetwithyounme.chettingapp.Model;

public class ViewChatList {
    private String user_Status;

    public ViewChatList(String user_Status) {
        this.user_Status = user_Status;
    }

    public ViewChatList() {
    }

    public String getUser_Status() {
        return user_Status;
    }

    public void setUser_Status(String user_Status) {
        this.user_Status = user_Status;
    }
}
