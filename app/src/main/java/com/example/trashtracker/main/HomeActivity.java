package com.example.trashtracker.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.trashtracker.R;
import com.example.trashtracker.interfaces.AuthCallBack;
import com.example.trashtracker.utils.Database;
import com.example.trashtracker.utils.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private AddFragment addFragment;
    private BottomNavigationView bottom_navigation;
    private FrameLayout homeFrame, profileFrame, addFrame;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        askPermissions();
        findViews();
        initViews();
        database = new Database();
        database.setAuthCallBack(new AuthCallBack() {
            @Override
            public void onCreateAccountComplete(boolean status, String msg) {

            }

            @Override
            public void updateUserInfoComplete(boolean status, String msg) {

            }

            @Override
            public void onLoginComplete(boolean status, String msg) {

            }

            @Override
            public void fetchUserInfoComplete(User user) {
                addFragment.setUser(user);
                profileFragment.setCurrentUser(user);
            }
        });

        database.getUserInfo(database.getCurrentUser().getUid());
        bottom_navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.menu_home){
                    homeFrame.setVisibility(View.VISIBLE);
                    addFrame.setVisibility(View.INVISIBLE);
                    profileFrame.setVisibility(View.INVISIBLE);
                }else if(item.getItemId() == R.id.menu_profile){
                    homeFrame.setVisibility(View.INVISIBLE);
                    addFrame.setVisibility(View.INVISIBLE);
                    profileFrame.setVisibility(View.VISIBLE);
                }else {
                    homeFrame.setVisibility(View.INVISIBLE);
                    addFrame.setVisibility(View.VISIBLE);
                    profileFrame.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });
    }

    private void askPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.CALL_PHONE}, 100);
        }
    }

    private void initViews() {
        homeFragment = new HomeFragment(this);
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame_home, homeFragment).commit();

        addFragment = new AddFragment(this);
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame_add_trash, addFragment).commit();

        profileFragment = new ProfileFragment(this);
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame_profile, profileFragment).commit();

        profileFrame.setVisibility(View.INVISIBLE);
        addFrame.setVisibility(View.INVISIBLE);
    }

    public void findViews(){
        bottom_navigation = findViewById(R.id.bottom_navigation);
        homeFrame = findViewById(R.id.main_frame_home);
        addFrame = findViewById(R.id.main_frame_add_trash);
        profileFrame = findViewById(R.id.main_frame_profile);
    }

}