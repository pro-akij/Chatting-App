package com.freechetwithyounme.chettingapp.Model;
import com.google.firebase.database.DatabaseReference;

public class User {
    private String name;
    private String status;
    private String imageuri;

    public User(String name, String status, String imageuri) {
        this.name = name;
        this.status = status;
        this.imageuri = imageuri;
    }

    public User() {
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
}
