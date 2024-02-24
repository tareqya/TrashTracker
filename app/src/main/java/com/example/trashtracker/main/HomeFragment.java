package com.example.trashtracker.main;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trashtracker.R;
import com.example.trashtracker.adapter.TrashPostAdapter;
import com.example.trashtracker.interfaces.TrashCallBack;
import com.example.trashtracker.utils.Database;
import com.example.trashtracker.utils.TrashPost;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private Context context;
    private Database db;
    private RecyclerView frag_home_RV_lostItems;

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
        db.setTrashCallBack(new TrashCallBack() {
            @Override
            public void onCreateTrashPostComplete(Task<Void> task) {

            }

            @Override
            public void onFetchTrashPostsComplete(ArrayList<TrashPost> trashPosts) {
                TrashPostAdapter trashPostAdapter = new TrashPostAdapter(context, trashPosts);
                frag_home_RV_lostItems.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                frag_home_RV_lostItems.setHasFixedSize(true);
                frag_home_RV_lostItems.setItemAnimator(new DefaultItemAnimator());
                frag_home_RV_lostItems.setAdapter(trashPostAdapter);
            }
        });

        db.fetchTrashPosts();
    }

    private void findViews(View root) {
        frag_home_RV_lostItems = root.findViewById(R.id.frag_home_RV_trashPlaces);
    }
}