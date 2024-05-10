package com.example.trashtracker.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import java.io.ByteArrayOutputStream;
import java.util.Random;

public class Generic {
    public static String getFileExtension(Activity activity, Uri uri){
        ContentResolver contentResolver = activity.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public static Uri getImageUri(Activity activity, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static int randomNumber(int min, int max){
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public static void openLocation(Context context, String locationName) {
        // Create a Uri object with the location name
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(locationName));
        // Open Google Maps app using implicit intent
        Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        context.startActivity(intent);
    }
}
