package com.example.trashtracker.utils;

import com.google.firebase.database.Exclude;

public class Uid {
    private String uid;
    public Uid(){}

    @Exclude
    public String getUid(){
        return uid;
    }

    public void setUid(String uid){
        this.uid = uid;
    }
}
