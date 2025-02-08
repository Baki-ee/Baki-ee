package com.example.roadsidecarhelp.model;

import java.io.Serializable;

public class Users implements Serializable {
// creates user specific details
    int id;
    String email;
    String password;
    String name;
    String username;
    String contact;
    String address;

    public Users() {
    }
//initializes all user attributes
    public Users(int id, String email, String password, String name, String username, String contact, String address) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.username = username;
        this.contact = contact;
        this.address = address;
    }
// all attributes except ID
    public Users(String email, String password, String name, String username, String contact, String address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.username = username;
        this.contact = contact;
        this.address = address;
    }
// controlled access to private data
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}