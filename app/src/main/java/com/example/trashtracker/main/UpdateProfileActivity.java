package com.example.trashtracker.main;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trashtracker.R;
import com.example.trashtracker.interfaces.AuthCallBack;
import com.example.trashtracker.utils.Database;
import com.example.trashtracker.utils.Generic;
import com.example.trashtracker.utils.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends AppCompatActivity {

    private User currentUser;
    private CircleImageView profileEdit_image;
    private FloatingActionButton profileEdit_FBTN_uploadImage;
    private TextInputLayout profileEdit_TF_name;
    private TextInputLayout profileEdit_TF_phone;
    private Button editProfile_BTN_update;
    private Uri selectedImageUri;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        Intent intent = getIntent();
        currentUser = (User) intent.getSerializableExtra("USER");

        findViews();
        initVars();
        displayUserUI();
    }

    private void displayUserUI() {
        if(currentUser.getImageUrl() != null){
            Glide.with(this).load(currentUser.getImageUrl()).into(profileEdit_image);
        }
        profileEdit_TF_name.getEditText().setText(currentUser.getName());
        profileEdit_TF_phone.getEditText().setText(currentUser.getPhone());
    }

    private void findViews() {
        profileEdit_image = findViewById(R.id.profileEdit_image);
        profileEdit_FBTN_uploadImage = findViewById(R.id.profileEdit_FBTN_uploadImage);
        profileEdit_TF_name = findViewById(R.id.profileEdit_TF_name);
        profileEdit_TF_phone = findViewById(R.id.profileEdit_TF_phone);
        editProfile_BTN_update = findViewById(R.id.editProfile_BTN_update);
    }

    private void initVars() {
        database = new Database();
        database.setAuthCallBack(new AuthCallBack() {
            @Override
            public void onCreateAccountComplete(boolean status, String msg) {

            }

            @Override
            public void updateUserInfoComplete(boolean status, String msg) {
                if(status){
                    Toast.makeText(UpdateProfileActivity.this, "User saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(UpdateProfileActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLoginComplete(boolean status, String msg) {

            }

            @Override
            public void fetchUserInfoComplete(User user) {

            }
        });
        editProfile_BTN_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // update user data
                updateUserProfile();
            }
        });

        profileEdit_FBTN_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageSourceDialog();
            }
        });
    }

    private void updateUserProfile() {
        currentUser.setName(profileEdit_TF_name.getEditText().getText().toString());
        currentUser.setPhone(profileEdit_TF_phone.getEditText().getText().toString());
        if(selectedImageUri != null){
            String imagePath = "Users/"+currentUser.getUid()+"."+Generic.getFileExtension(this, selectedImageUri);
            if(database.uploadImage(selectedImageUri, imagePath)){
                currentUser.setImagePath(imagePath);
            }
        }

        database.updateUserInfo(currentUser);
    }


    private void showImageSourceDialog() {
        CharSequence[] items = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                                final InputStream imageStream = UpdateProfileActivity.this.getContentResolver().openInputStream(selectedImageUri);
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                profileEdit_image.setImageBitmap(selectedImage);
                            }
                            catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Toast.makeText(UpdateProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(UpdateProfileActivity.this, "Gallery canceled", Toast.LENGTH_SHORT).show();
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
                            profileEdit_image.setImageBitmap(bitmap);
                            selectedImageUri = Generic.getImageUri(UpdateProfileActivity.this, bitmap);
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(UpdateProfileActivity.this, "Camera canceled", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
}