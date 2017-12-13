package com.example.nabahat.studentapp;

/**
 * Created by Nabahat on 12/13/2017.
 */

public class Feedback {

    public Feedback(String studentid, String driverid, String feedback) {
        Studentid = studentid;
        Driverid = driverid;
        Feedback = feedback;
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

    String Studentid;
    String Driverid;
    String Feedback;
    Feedback(){}

}
