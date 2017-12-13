package com.example.nabahat.studentapp;

/**
 * Created by Nabahat on 12/11/2017.
 */

public class Driver {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getBusnumber() {
        return busnumber;
    }

    public void setBusnumber(String busnumber) {
        this.busnumber = busnumber;
    }

    public String id;
    public String username;
    public String email;
    public String password;
    public String phonenumber;
    public String busnumber;




    public Driver() {

    }

    public Driver(String id, String username, String email, String password, String phonenumber, String busnumber) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phonenumber = phonenumber;
        this.busnumber = busnumber;

    }

}
