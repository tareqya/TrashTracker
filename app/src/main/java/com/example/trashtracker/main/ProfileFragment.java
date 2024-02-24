package com.example.trashtracker.main;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trashtracker.R;
import com.example.trashtracker.utils.User;

public class ProfileFragment extends Fragment {

    private User currentUser;

    public ProfileFragment(Context context) {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public void setUser(User user) {
        this.currentUser = user;
    }
}