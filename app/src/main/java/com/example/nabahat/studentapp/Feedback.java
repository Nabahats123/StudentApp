package com.example.nabahat.studentapp;

import java.util.Date;

/**
 * Created by Nabahat on 12/13/2017.
 */

public class Feedback {


    String Feedback;
    float Rating;
    String Date;
    String Studentid;
    String Driverid;

    Feedback(){}

    public Feedback(String studentid, String driverid, String feedback,String date , float rating) {
        Studentid = studentid;
        Driverid = driverid;
        Feedback = feedback;
        Rating = rating;
        Date = date;
    }

    public Feedback(String studentid, String driverid, String feedback, float rating) {
        Studentid = studentid;
        Driverid = driverid;
        Feedback = feedback;
        Rating = rating;
    }

    public String getStudentid() {
        return Studentid;
    }

    public void setStudentid(String studentid) {
        Studentid = studentid;
    }

    public String getDriverid() {
        return Driverid;
    }

    public void setDriverid(String driverid) {
        Driverid = driverid;
    }

    public String getFeedback() {
        return Feedback;
    }

    public void setFeedback(String feedback) {
        Feedback = feedback;
    }







    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }


}
