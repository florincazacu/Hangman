package com.hangman.app;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    public void createScoresReference() {
        scoresReference = FirebaseDatabase.getInstance().getReference("scores");
        scoresReference.keepSynced(true);
    }

    public void pushScoreToFirebase(Score scores) {
        scoresReference.push().setValue(scores);
    }
}
