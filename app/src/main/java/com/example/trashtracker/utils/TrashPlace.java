package com.example.trashtracker.utils;

import com.google.firebase.database.Exclude;

import java.util.Date;

public class TrashPlace extends Uid{

    private String title;
    private String location;
    private Date date;
    private String imagePath;
    private String imageUrl;
    private String userUid;
    private String userPhone;

    public TrashPlace() {}

    public String getTitle() {
        return title;
    }

    public TrashPlace setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public TrashPlace setLocation(String location) {
        this.location = location;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public TrashPlace setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getImagePath() {
        return imagePath;
    }

    public TrashPlace setImagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }
    @Exclude
    public String getImageUrl() {
        return imageUrl;
    }

    public TrashPlace setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getUserUid() {
        return userUid;
    }

    public TrashPlace setUserUid(String userUid) {
        this.userUid = userUid;
        return this;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public TrashPlace setUserPhone(String userPhone) {
        this.userPhone = userPhone;
        return this;
    }
}
