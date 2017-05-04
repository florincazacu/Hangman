package com.hangman.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "MainActivityHangman";
    private static final int RC_SIGN_IN = 12345;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    Button testButton;
    Button categoryButton;

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
        testButton = (Button) findViewById(R.id.test_button);
        testButton.setOnClickListener(this);
        categoryButton= (Button) findViewById(R.id.category_button);
        categoryButton.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Intent myIntent = new Intent( MainActivity.this, SignInActivity.class);
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

    @Override
    public void onClick(View v) {
        if (user != null) {
            Intent myIntent;
            switch (v.getId()) {
                case R.id.test_button:
                        Log.d(TAG, "test button text: " + testButton.getText().toString().toLowerCase());
                        myIntent = new Intent(MainActivity.this, GameActivity.class);
                        myIntent.putExtra("category", testButton.getText().toString().toLowerCase());
                        startActivity(myIntent);
                        break;
                case R.id.category_button:
                        Log.d(TAG, "catgory button text: " + categoryButton.getText().toString().toLowerCase());
                        myIntent = new Intent(MainActivity.this, GameActivity.class);
                        myIntent.putExtra("category", categoryButton.getText().toString().toLowerCase());
                        startActivity(myIntent);
            }
        }

    }
}
