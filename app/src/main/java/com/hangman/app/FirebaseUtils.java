package com.hangman.app;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

/**
 * Created by Florin on 01-08-2017.
 */

public class FirebaseUtils {

    private static final String TAG = "FirebaseUtils";

    private DatabaseReference scoresReference;
    private String selectedCategory;
    private String gsKey;
    private String mUsername;

    public FirebaseUtils(String selectedCategory, String gsKey) {
        this.selectedCategory = selectedCategory;
        this.gsKey = gsKey;
    }

    public FirebaseStorage getFirebaseStorage() {
        return FirebaseStorage.getInstance();
    }

    public StorageReference getStorageReference() {
        return getFirebaseStorage().getReferenceFromUrl(gsKey);
    }

    public void downloadCategoryFromFirebase(File downloadCategory) {
        getStorageReference().getFile(downloadCategory).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e(TAG, "FirebaseUtils onSuccess");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e(TAG, "onFailure: " + exception.getMessage());
                exception.printStackTrace();
            }
        });
    }


    public void createScoresReference() {
        scoresReference = FirebaseDatabase.getInstance().getReference("scores");
        scoresReference.keepSynced(true);
    }

    public void pushScoreToFirebase(Score scores) {
        scoresReference.push().setValue(scores);
    }

    public FirebaseUser getFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public String getGsKey() {
        return gsKey;
    }

    public String getUsername() {
            return mUsername = getFirebaseUser().getDisplayName();
    }
}
