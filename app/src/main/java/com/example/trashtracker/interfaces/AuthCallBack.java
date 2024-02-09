package com.example.trashtracker.interfaces;

import com.example.trashtracker.utils.User;

public interface AuthCallBack {
    void onCreateAccountComplete(boolean status, String msg);
    void updateUserInfoComplete(boolean status, String msg);
    void onLoginComplete(boolean status, String msg);
    void fetchUserInfoComplete(User user);
}
