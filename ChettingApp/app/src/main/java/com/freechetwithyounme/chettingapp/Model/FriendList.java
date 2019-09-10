package com.freechetwithyounme.chettingapp.Model;

public class FriendList {

    private String name;
    private String status;
    private String imageuri;
    private String date;

    public FriendList(String name, String status, String imageuri, String date) {
        this.name = name;
        this.status = status;
        this.imageuri = imageuri;
        this.date = date;
    }

    public FriendList() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
