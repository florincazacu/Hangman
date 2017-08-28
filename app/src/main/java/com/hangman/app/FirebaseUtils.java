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

    private DatabaseReference scoresReference;
    private String selectedCategory;
    private String gsKey;


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

    public void pushScoreToFirebase(Score scores) {
        scoresReference.push().setValue(scores);
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public FirebaseUser getFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public String getGsKey() {
        return gsKey;
    }
}
