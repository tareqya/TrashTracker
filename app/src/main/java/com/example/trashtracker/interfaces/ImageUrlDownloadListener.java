package com.example.trashtracker.interfaces;

public interface ImageUrlDownloadListener {
    void onImageUrlDownloaded(String imageUrl);
    void onImageUrlDownloadFailed(String errorMessage);
}
