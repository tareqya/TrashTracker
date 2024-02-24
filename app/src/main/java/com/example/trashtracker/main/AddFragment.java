package com.example.trashtracker.main;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.trashtracker.R;
import com.example.trashtracker.interfaces.TrashCallBack;
import com.example.trashtracker.utils.Database;
import com.example.trashtracker.utils.Generic;
import com.example.trashtracker.utils.TrashPost;
import com.example.trashtracker.utils.User;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

public class AddFragment extends Fragment {
    private int SCORE_PER_POST = 100;
    private TextInputLayout frag_add_post_TIL_title;
    private TextInputLayout frag_add_post_TIL_location;
    private ImageView frag_add_post_IV_image;
    private Button frag_add_post_BTN_upload;
    private Context context;
    private Database database;
    private Uri selectedImageUri;
    private User currentUser;
    public AddFragment(Context context) {
        // Required empty public constructor
        this.context = context;
        this.database = new Database();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        findViews(view);
        initVars();
        return view;
    }

    private void findViews(View view) {
        frag_add_post_TIL_title = view.findViewById(R.id.frag_add_post_TIL_title);
        frag_add_post_TIL_location = view.findViewById(R.id.frag_add_post_TIL_location);
        frag_add_post_IV_image = view.findViewById(R.id.frag_add_post_IV_image);
        frag_add_post_BTN_upload = view.findViewById(R.id.frag_add_post_BTN_upload);

    }
    public void setUser(User user) {
        currentUser = user;
    }

    private void initVars() {

        database.setTrashCallBack(new TrashCallBack() {
            @Override
            public void onCreateTrashPostComplete(Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "Post added successfully", Toast.LENGTH_SHORT).show();
                    frag_add_post_TIL_title.getEditText().setText("");
                    frag_add_post_TIL_location.getEditText().setText("");
                    selectedImageUri = null;
                    frag_add_post_IV_image.setImageResource(R.drawable.baseline_add_a_photo_24);
                    //update user score per each added post
                    currentUser.setScore(currentUser.getScore() + SCORE_PER_POST);
                    database.updateUserInfo(currentUser);

                }else{
                    String err = task.getException().getMessage().toString();
                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFetchTrashPostsComplete(ArrayList<TrashPost> trashPosts) {

            }
        });

        frag_add_post_BTN_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // upload new trash post
                String title = frag_add_post_TIL_title.getEditText().getText().toString();
                String location = frag_add_post_TIL_location.getEditText().getText().toString();

                if(title.isEmpty()){
                    Toast.makeText(context, "Title is required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(location.isEmpty()){
                    Toast.makeText(context, "Location is required!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(selectedImageUri == null){
                    Toast.makeText(context, "Image is required!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String uid = database.getCurrentUser().getUid();
                // Posts/alsnfasigfias_12451251.jpg
                String imageName = uid+"_"+Generic.randomNumber(1_000_000, 9_999_999);
                String imagePath = "Posts/"+ imageName + "." + Generic.getFileExtension((Activity) context, selectedImageUri);

                if(database.uploadImage(selectedImageUri, imagePath)){
                    TrashPost trashPost = new TrashPost()
                            .setTitle(title)
                            .setLocation(location)
                            .setUser(currentUser)
                            .setCreatedDate(new Date())
                            .setImagePath(imagePath);
                    database.uploadTrashPost(trashPost);
                }else{
                    Toast.makeText(context, "Failed to upload the image, please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        frag_add_post_IV_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageSourceDialog();
            }
        });
    }

    private void showImageSourceDialog() {
        CharSequence[] items = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Image Source");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch (which) {
                    case 0:
                        openCamera();
                        break;
                    case 1:
                        openGallery();
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraResults.launch(intent);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        gallery_results.launch(intent);
    }

    private final ActivityResultLauncher<Intent> gallery_results = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    switch (result.getResultCode()) {
                        case Activity.RESULT_OK:
                            try {
                                Intent intent = result.getData();
                                selectedImageUri = intent.getData();
                                final InputStream imageStream = context.getContentResolver().openInputStream(selectedImageUri);
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                frag_add_post_IV_image.setImageBitmap(selectedImage);
                            }
                            catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(context, "Gallery canceled", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });

    private final ActivityResultLauncher<Intent> cameraResults = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    switch (result.getResultCode()) {
                        case Activity.RESULT_OK:
                            Intent intent = result.getData();
                            Bitmap bitmap = (Bitmap)  intent.getExtras().get("data");
                            frag_add_post_IV_image.setImageBitmap(bitmap);
                            selectedImageUri = Generic.getImageUri(((Activity) context), bitmap);
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(context, "Camera canceled", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });


}