package com.hangman.app;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Florin on 13-04-2017.
 */

public class MyFirebaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
