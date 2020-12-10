package com.developer.pollingmanagementsystem;

public class User {

    public String name,contact,email,password,stationId,officerPosition,zonalOfficierId;

    public User() {
    }

    public User(String name, String contact, String email, String password, String stationId, String officerPosition, String zonalOfficierId) {
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.password = password;
        this.stationId = stationId;
        this.officerPosition = officerPosition;
        this.zonalOfficierId = zonalOfficierId;
    }

    public String getZonalOfficierId() {
        return zonalOfficierId;
    }

    public void setZonalOfficierId(String zonalOfficierId) {
        this.zonalOfficierId = zonalOfficierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getOfficerPosition() {
        return officerPosition;
    }

    public void setOfficerPosition(String officerPosition) {
        this.officerPosition = officerPosition;
    }
}
