package com.hangman.app;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

    public void createScoresReference() {
        scoresReference = FirebaseDatabase.getInstance().getReference("scores");
        scoresReference.keepSynced(true);
    }

    public void updateScoreInFirebase(Score playerScore) {
        FirebaseDatabase.getInstance().getReference().child("scores").child(mUsername).setValue(playerScore);
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
