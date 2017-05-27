package com.hangman.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivityHangman";
    private static final int RC_SIGN_IN = 12345;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private Category mCategory;

    private GridLayoutManager mGridLayoutManager;

    private DatabaseReference categoriesReference;

    private ArrayList<Category> categoriesMap = new ArrayList<>();

    FirebaseUser user;

    private String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mGridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        categoriesReference = FirebaseDatabase.getInstance().getReference().child("categories");

        categoriesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //iterating through all the values in database

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        Category category = snap.getValue(Category.class);
                        Log.d(TAG, "category " + category);
                        Log.d(TAG, "category getName " + category.getName());
                        Log.d(TAG, "category getGSRef " + category.getGs_reference());
//                        categoriesMap.put(category.getGs_reference(), category.getName());
                        categoriesMap.add(category);
                        Log.d(TAG, "categoriesMap " + categoriesMap);
                    }
                } else {
                    Log.e(TAG, "onDataChange: NO DATA");
                }
                    Log.d(TAG, "categoriesList " + categoriesMap);
                    mAdapter = new RecyclerAdapter(categoriesMap);
                    mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Intent myIntent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(myIntent);
        }
    }


    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
