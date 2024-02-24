package com.example.trashtracker.interfaces;

import com.example.trashtracker.utils.TrashPost;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public interface TrashCallBack {
    void onCreateTrashPostComplete(Task<Void> task);
    void onFetchTrashPostsComplete(ArrayList<TrashPost> trashPosts);
}
