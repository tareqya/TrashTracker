package com.example.trashtracker.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.trashtracker.R;
import com.example.trashtracker.utils.Generic;
import com.example.trashtracker.utils.TrashPost;

import java.util.ArrayList;

public class TrashPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private ArrayList<TrashPost> trashPosts;
    private Context context;

    public TrashPostAdapter(Context context, ArrayList<TrashPost>trashPosts){
        this.trashPosts = trashPosts;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trash_post_item, parent, false);
        return new TrashPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TrashPostViewHolder trashPostViewHolder = (TrashPostViewHolder) holder;
        TrashPost trashPost = getItem(position);
        if(trashPost.getUser() != null){
            trashPostViewHolder.trash_TV_name.setText(trashPost.getUser().getName());
            trashPostViewHolder.trash_TV_date.setText(trashPost.getCreatedDate().toLocaleString());
            Glide.with(context).load(trashPost.getImageUrl()).into(trashPostViewHolder.trash_IV_postImage);
            trashPostViewHolder.trash_TV_Title.setText(trashPost.getTitle());
        }
    }

    public TrashPost getItem(int pos){
        return this.trashPosts.get(pos);
    }
    @Override
    public int getItemCount() {
        return this.trashPosts.size();
    }

    public class TrashPostViewHolder extends  RecyclerView.ViewHolder {
        public TextView trash_TV_Title;
        public Button trash_BTN_openInMap;
        public Button trash_BTN_call;
        public TextView trash_TV_name;
        public TextView trash_TV_date;
        public ImageView trash_IV_postImage;

        public TrashPostViewHolder(@NonNull View itemView) {
            super(itemView);

            trash_TV_Title = itemView.findViewById(R.id.trash_TV_Title);
            trash_BTN_openInMap = itemView.findViewById(R.id.trash_BTN_openInMap);
            trash_BTN_call = itemView.findViewById(R.id.trash_BTN_call);
            trash_TV_name = itemView.findViewById(R.id.trash_TV_name);
            trash_TV_date = itemView.findViewById(R.id.trash_TV_date);
            trash_IV_postImage = itemView.findViewById(R.id.trash_IV_postImage);


            trash_BTN_openInMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    TrashPost trashPost = getItem(pos);
                    Generic.openLocation(context, trashPost.getLocation());
                }
            });

            trash_BTN_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    TrashPost trashPost = getItem(pos);
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + trashPost.getUser().getPhone()));
                    ((Activity)context).startActivity(intent);
                }
            });
        }
    }
}
