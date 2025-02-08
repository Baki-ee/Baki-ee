package com.example.roadsidecarhelp.model;
// properties of contact
public class Contact {
    private int id;
    private String name;
    private String phoneNumber;
    private String relationship;
//makes a new contact and assigns it values
    public Contact(int id, String name, String phoneNumber, String relationship) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.relationship = relationship;
    }
//returns variables when asked to println
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
