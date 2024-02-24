package com.example.trashtracker.utils;

import com.google.firebase.database.Exclude;

import java.util.Date;

public class TrashPost extends Uid{
    private String title;
    private String location;
    private User user;
    private String imagePath;
    private String imageUrl;
    private Date createdDate;

    public TrashPost() {
        this.createdDate = new Date();
    }

    public String getTitle() {
        return title;
    }

    public TrashPost setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public TrashPost setLocation(String location) {
        this.location = location;
        return this;
    }

    public TrashPost setUser(User user){
        this.user = user;
        return this;
    }

    public User getUser(){
        return this.user;
    }
    public String getImagePath() {
        return imagePath;
    }

    public TrashPost setImagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }
    @Exclude
    public String getImageUrl() {
        return imageUrl;
    }

    public TrashPost setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public TrashPost setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }
}
