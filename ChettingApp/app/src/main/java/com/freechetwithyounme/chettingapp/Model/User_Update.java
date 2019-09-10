package com.freechetwithyounme.chettingapp.Model;

public class User_Update {
    private String status;
    private String location;
    private String occupation;
    private String imageURI;

    public User_Update(String status, String location, String occupation, String imageURI) {
        this.status = status;
        this.location = location;
        this.occupation = occupation;
        this.imageURI = imageURI;
    }

    public User_Update() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }
}