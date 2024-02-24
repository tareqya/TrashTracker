package com.example.trashtracker.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;
import java.util.Locale;
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
        // Check if Google Maps is installed
//        if (!isGoogleMapsInstalled(context)) {
//            Toast.makeText(context, "Please install Google Maps to use this feature.", Toast.LENGTH_LONG).show();
//            return;
//        }

        // Encode location name for URL safety
        String encodedLocationName = Uri.encode(locationName);

        // Build Google Maps URL with user's preferred language
        String language = Locale.getDefault().getLanguage();
        String googleMapsUrl = "https://www.google.com/maps/search/?q=" + encodedLocationName + "&language=" + language;

        // Open Google Maps app using implicit intent
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(googleMapsUrl));
        context.startActivity(intent);
    }

    private static boolean isGoogleMapsInstalled(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo("com.google.android.apps.maps", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
