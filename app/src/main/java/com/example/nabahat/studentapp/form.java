package com.example.nabahat.studentapp;

/**
 * Created by Nabahat on 2/15/2018.
 */

public class form {
    String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getBusNumber() {
        return BusNumber;
    }

    public void setBusNumber(String busNumber) {
        BusNumber = busNumber;
    }

    public String getStopName() {
        return StopName;
    }

    public void setStopName(String stopName) {
        StopName = stopName;
    }



    public form(String name, String email, String phoneNumber, String address, String busNumber, String stopName) {
        Name = name;
        Email = email;
        PhoneNumber = phoneNumber;
        Address = address;
        BusNumber = busNumber;
        StopName = stopName;
    }
    String Email;
    String PhoneNumber;
    String Address;
    String BusNumber;
    String StopName;
}
