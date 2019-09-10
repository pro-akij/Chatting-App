package com.freechetwithyounme.chettingapp.Model;

public class GridModel {

    private String name;
    private String location;
    private String imageuri;

    public GridModel(String name, String location, String imageuri) {
        this.name = name;
        this.location = location;
        this.imageuri = imageuri;
    }

    public GridModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }
}
