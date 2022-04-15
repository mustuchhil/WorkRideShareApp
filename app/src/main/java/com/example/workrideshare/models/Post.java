package com.example.workrideshare.models;

import com.google.firebase.Timestamp;

public class Post {
    private String postID;
//    private String id;
    private String fromDest;
    private String toDest;
    private String desc;
    private int numOfSeats;
    private String contact;
    private String postCity;
    private String userID;
    private Timestamp dateCreated;

    public Post(String fromDest, String toDest, String desc, int numOfSeats, String contact, String postCity, String userID, Timestamp dateCreated, String postID) {
        this.postID = postID;
        this.fromDest = fromDest;
        this.toDest = toDest;
        this.desc = desc;
        this.numOfSeats = numOfSeats;
        this.contact = contact;
        this.postCity = postCity;
        this.userID = userID;
        this.dateCreated = dateCreated;
    }
    public Post(){ }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getFromDest() {
        return fromDest;
    }

    public void setFromDest(String fromDest) {
        this.fromDest = fromDest;
    }

    public String getToDest() {
        return toDest;
    }

    public void setToDest(String toDest) {
        this.toDest = toDest;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getNumOfSeats() {
        return numOfSeats;
    }

    public void setNumOfSeats(int numOfSeats) {
        this.numOfSeats = numOfSeats;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPostCity() {
        return postCity;
    }

    public void setPostCity(String postCity) {
        this.postCity = postCity;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "Post{" +
//                "id='" + id + '\'' +
                ", fromDest='" + fromDest + '\'' +
                ", toDest='" + toDest + '\'' +
                ", desc='" + desc + '\'' +
                ", numOfSeats=" + numOfSeats +
                ", contact='" + contact + '\'' +
                ", postCity='" + postCity + '\'' +
                ", postID='" + postID + '\'' +
                ", userID='" + userID + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
