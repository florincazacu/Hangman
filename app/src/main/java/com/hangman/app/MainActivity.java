package com.hangman.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivityHangman";
    private static final int RC_SIGN_IN = 12345;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private DatabaseReference categoriesReference;

    private ArrayList<String> categories = new ArrayList<>();

    FirebaseUser user;

    public Category category;

    private String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);

        categoriesReference = FirebaseDatabase.getInstance().getReference().child("categories");

        categoriesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //iterating through all the values in database
                if (snapshot.exists()) {
                    for (DataSnapshot categoriesSnapshot : snapshot.getChildren()) {
                        category = categoriesSnapshot.getValue(Category.class);
                        String currentCategory = category.getName();
                        categories.add(currentCategory);
                    }
                    createButtonsInMain();
                }
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

    public void populateFirstLayout() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.category_buttons_layout_column1);
        for (int j = 0; j < categories.size(); j++) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
            if (j % 2 == 0) {
                Button categoryButton =(Button) LayoutInflater.from(this.getApplicationContext()).inflate(R.layout.view_category_button, row, false);
                categoryButton.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
                categoryButton.setText(String.valueOf(categories.get(j)));
                categoryButton.setTag(categories.get(j));
                categoryButton.setOnClickListener(this);
                row.addView(categoryButton);
            }
            layout.addView(row);
        }
    }

    public void populateSecondLayout() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.category_buttons_layout_column2);
        for (int j = 0; j < categories.size(); j++) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
            if (j % 2 != 0) {
//                Button categoryButton = new Button(this);
                Button categoryButton =(Button) LayoutInflater.from(this.getApplicationContext()).inflate(R.layout.view_category_button, row, false);
                categoryButton.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
                categoryButton.setText(String.valueOf(categories.get(j)));
                categoryButton.setTag(categories.get(j));
                categoryButton.setOnClickListener(this);
                row.addView(categoryButton);
            }
            layout.addView(row);
        }
    }

    public void createButtonsInMain() {
        populateFirstLayout();
        populateSecondLayout();
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

    @Override
    public void onClick(View v) {

        Log.d(TAG, "tag: " + v.getTag());
        Intent myIntent;
        myIntent = new Intent(MainActivity.this, GameActivity.class);
        myIntent.putExtra("category", v.getTag().toString());
        startActivity(myIntent);


        switch (v.getId()) {
//            case R.id.anatomy_button:
//                Log.d(TAG, "test button text: " + anatomyButton.getText().toString().toLowerCase());
//                myIntent = new Intent(MainActivity.this, GameActivity.class);
//                myIntent.putExtra("category", anatomyButton.getText().toString().toLowerCase());
//                startActivity(myIntent);
//                break;
//            case R.id.animals_button:
//                Log.d(TAG, "catgory button text: " + animalsButton.getText().toString().toLowerCase());
//                myIntent = new Intent(MainActivity.this, GameActivity.class);
//                myIntent.putExtra("category", animalsButton.getText().toString().toLowerCase());
//                startActivity(myIntent);
//                break;
//            case R.id.generate_id:
//                Log.e(TAG, "generated id " + FirebasePushIDGenerator.generatePushId());
//                break;
        }
    }
}
