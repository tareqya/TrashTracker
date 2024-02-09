package com.example.trashtracker.main;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trashtracker.R;
import com.example.trashtracker.interfaces.AuthCallBack;
import com.example.trashtracker.utils.Database;
import com.example.trashtracker.utils.TrashPlace;
import com.example.trashtracker.utils.User;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private final int SCORE = 100;
    private Context context;
    private Database db;
    private ArrayList<TrashPlace> trashPlaces;
    private RecyclerView frag_home_RV_lostItems;
    private User currentUser;
    public HomeFragment(Context context) {
        this.context = context;
        db = new Database();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        findViews(root);
        initVars();
        return root;
    }

    private void initVars() {
        db.setAuthCallBack(new AuthCallBack() {
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
                currentUser = user;
            }
        });

        if(db.getCurrentUser() != null)
            db.getUserInfo(db.getCurrentUser().getUid());
    }

    private void findViews(View root) {
        frag_home_RV_lostItems = root.findViewById(R.id.frag_home_RV_trashPlaces);
    }
}