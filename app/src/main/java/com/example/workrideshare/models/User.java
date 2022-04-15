package com.example.workrideshare.models;

import java.util.Date;

public class User {
    private String id;
    private String Name;
    private String Email;
    private String PhoneNum;

    public User(String Name, String Email, String PhoneNum, String id) {
        this.Name = Name;
        this.Email = Email;
        this.PhoneNum = PhoneNum;
        this.id = id;
    }

    public User(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getPhoneNum() {
        return PhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", Name='" + Name + '\'' +
                ", Email='" + Email + '\'' +
                ", PhoneNum=" + PhoneNum +
                '}';
    }
}
