package com.example.trashtracker.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.trashtracker.R;
import com.example.trashtracker.auth.LoginActivity;
import com.example.trashtracker.utils.Database;
import com.example.trashtracker.utils.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private User currentUser;
    private Context context;
    private CircleImageView profile_CIV_image;
    private TextView profile_TV_name;
    private TextView profile_TV_email;
    private CardView profile_CV_editDetails;
    private CardView profile_CV_logout;
    private Database database;


    public ProfileFragment(Context context) {
        // Required empty public constructor
        this.context = context;
        database = new Database();
    }

    public void setCurrentUser(User user){
        this.currentUser = user;
        displayUserUI();
    }

    private void displayUserUI() {
        if(currentUser.getImageUrl() != null){
            Glide.with(context).load(currentUser.getImageUrl()).into(profile_CIV_image);
        }
        profile_TV_name.setText(currentUser.getName());
        profile_TV_email.setText(currentUser.getEmail());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        findViews(view);
        initVars();
        return view;
    }

    private void initVars() {

        profile_CV_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do logout
                Intent intent = new Intent((Activity) context, LoginActivity.class);
                database.logout();
                startActivity(intent);
                ((Activity) context).finish();
            }
        });

        profile_CV_editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to update profile screen
                Intent intent = new Intent((Activity) context, UpdateProfileActivity.class);
                intent.putExtra("USER", currentUser);
                startActivity(intent);
            }
        });
    }

    private void findViews(View view) {
        profile_CIV_image = view.findViewById(R.id.profile_CIV_image);
        profile_TV_email = view.findViewById(R.id.profile_TV_email);
        profile_TV_name = view.findViewById(R.id.profile_TV_name);
        profile_CV_editDetails = view.findViewById(R.id.profile_CV_editDetails);
        profile_CV_logout = view.findViewById(R.id.profile_CV_logout);

    }
}