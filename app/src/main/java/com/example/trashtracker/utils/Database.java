package com.example.trashtracker.utils;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.trashtracker.interfaces.AuthCallBack;
import com.example.trashtracker.interfaces.TrashCallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class Database {
    public static final String USERS_TABLE = "Users";
    public static final String TRASH_POSTS_TABLE = "TrashPosts";
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseStorage mStorage;
    private AuthCallBack authCallBack;
    private TrashCallBack trashCallBack;

    public Database(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();
    }
    public void setAuthCallBack(AuthCallBack authCallBack) {
        this.authCallBack = authCallBack;
    }
    public void setTrashCallBack(TrashCallBack trashCallBack){
        this.trashCallBack = trashCallBack;
    }
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void login(User user) {
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            authCallBack.onLoginComplete(true, "");
                        }else{
                            authCallBack.onLoginComplete(false, task.getException().getMessage());
                        }
                    }
                }) ;
    }


    public void createNewUser(User user) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            authCallBack.onCreateAccountComplete(true, "");
                        }else{
                            authCallBack.onCreateAccountComplete(false,
                                    task.getException().getMessage());
                        }
                    }
                });
    }

    public void updateUserInfo(User user) {
        mDatabase.getReference(USERS_TABLE).child(user.getUid()).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(authCallBack != null){
                            if(task.isSuccessful()){
                                authCallBack.updateUserInfoComplete(true, "");
                            }else{
                                authCallBack.updateUserInfoComplete(false, task.getException().getMessage());
                            }
                        }

                    }
                });
    }

    public void logout() {
        mAuth.signOut();
    }

    public void getUserInfo(String uid) {
        mDatabase.getReference(USERS_TABLE).child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null){
                    user.setUid(uid);
                    if(user.getImagePath() != null){
                        String imageUrl = downloadImageUrl(user.getImagePath());
                        user.setImageUrl(imageUrl);
                    }
                }
                authCallBack.fetchUserInfoComplete(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String downloadImageUrl(String imagePath){
        Task<Uri> downloadImageTask = mStorage.getReference().child(imagePath).getDownloadUrl();
        while (!downloadImageTask.isComplete() && !downloadImageTask.isCanceled());
        return downloadImageTask.getResult().toString();
    }

    public boolean uploadImage(Uri imageUri, String imagePath){
        try{
            UploadTask uploadTask = mStorage.getReference(imagePath).putFile(imageUri);
            while (!uploadTask.isComplete() && !uploadTask.isCanceled());
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage().toString());
            return false;
        }
    }

    public void uploadTrashPost(TrashPost post){
        this.mDatabase.getReference(TRASH_POSTS_TABLE).push().setValue(post)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        trashCallBack.onCreateTrashPostComplete(task);
                    }
                });
    }

    public void fetchTrashPosts(){
        this.mDatabase.getReference(TRASH_POSTS_TABLE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<TrashPost> trashPosts = new ArrayList<>();
                for(DataSnapshot data : snapshot.getChildren()){
                    TrashPost trashPost = data.getValue(TrashPost.class);
                    trashPost.setUid(data.getKey());
                    String imageUrl = downloadImageUrl(trashPost.getImagePath());
                    trashPost.setImageUrl(imageUrl);
                    trashPosts.add(trashPost);
                }

                trashCallBack.onFetchTrashPostsComplete(trashPosts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
