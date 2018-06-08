package com.example.nabahat.studentapp;

/**
 * Created by Nabahat on 2/15/2018.
 */

public class form {
    String RegistrationNumber;
    String Program;

    public form(String registrationNumber, String program, String semester, String name, String fatherName, String email, String phoneNumber, String emergencyNumber, String address, String busNumber, String stopName, String date, String time) {
        RegistrationNumber = registrationNumber;
        Program = program;
        Semester = semester;
        Name = name;
        FatherName = fatherName;
        Email = email;
        PhoneNumber = phoneNumber;
        EmergencyNumber = emergencyNumber;
        Address = address;
        BusNumber = busNumber;
        StopName = stopName;
        this.date = date;
        this.time = time;
    }

    String Semester;
    String Name;
    String FatherName;
    String Email;
    String PhoneNumber;
    String EmergencyNumber;
    String Address;
    String BusNumber;
    String StopName;
    String date;
    String time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRegistrationNumber() {
        return RegistrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        RegistrationNumber = registrationNumber;
    }

    public String getProgram() {
        return Program;
    }

    public void setProgram(String program) {
        Program = program;
    }

    public String getSemester() {
        return Semester;
    }

    public void setSemester(String semester) {
        Semester = semester;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFatherName() {
        return FatherName;
    }

    public void setFatherName(String fatherName) {
        FatherName = fatherName;
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

    public String getEmergencyNumber() {
        return EmergencyNumber;
    }

    public void setEmergencyNumber(String emergencyNumber) {
        EmergencyNumber = emergencyNumber;
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



    form (){}








}
