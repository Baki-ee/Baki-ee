package com.example.roadsidecarhelp.model;
// defines the attributes of services
public class Service {
    private String name;
    private String type;
    private double latitude;
    private double longitude;
    private String contact;
//assigns values to services
    public Service(String name, String type, double latitude, double longitude, String contact) {
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.contact = contact;
    }
//private access to variables
    // Getters
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getContact() {
        return contact;
    }
}

