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

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivityHangman";
    private static final int RC_SIGN_IN = 12345;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    Button anatomyButton;
    Button animalsButton;
    Button buildingsButton;
    Button bussinessButton;
    Button carsAndPartsButton;
    Button clothesAndShoesButton;
    Button computersButton;
    Button fantasyButton;
    Button foodsAndDrinksButton;
    Button fruitsButton;
    Button geographyButton;
    Button healthButton;
    Button houseAndMaterialsButton;
    Button jobsAndOccupationsButton;
    Button measurementButton;
    Button musicButton;
    Button randomButton;
    Button schoolButton;
    Button scienceButton;
    Button shapesButton;
    Button sportsButton;
    Button toolsButton;
    Button transportationButton;
    Button treesAndFlowersButton;
    Button weatherButton;

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
        anatomyButton = (Button) findViewById(R.id.anatomy_button);
        anatomyButton.setOnClickListener(this);
        animalsButton = (Button) findViewById(R.id.animals_button);
        animalsButton.setOnClickListener(this);
        buildingsButton = (Button) findViewById(R.id.buildings_button);
        buildingsButton.setOnClickListener(this);
        bussinessButton = (Button) findViewById(R.id.bussiness_button);
        bussinessButton.setOnClickListener(this);
        carsAndPartsButton = (Button) findViewById(R.id.cars_and_parts_button);
        carsAndPartsButton.setOnClickListener(this);
        clothesAndShoesButton = (Button) findViewById(R.id.clothes_and_shoes_button);
        clothesAndShoesButton.setOnClickListener(this);
        computersButton = (Button) findViewById(R.id.computers_button);
        computersButton.setOnClickListener(this);
        fantasyButton = (Button) findViewById(R.id.fantasy_button);
        fantasyButton.setOnClickListener(this);
        foodsAndDrinksButton = (Button) findViewById(R.id.foods_and_drinks_button);
        foodsAndDrinksButton.setOnClickListener(this);
        fruitsButton = (Button) findViewById(R.id.fruits_button);
        fruitsButton.setOnClickListener(this);
        geographyButton = (Button) findViewById(R.id.geography_button);
        geographyButton.setOnClickListener(this);
        healthButton = (Button) findViewById(R.id.health_button);
        healthButton.setOnClickListener(this);
        houseAndMaterialsButton = (Button) findViewById(R.id.house_and_materials_button);
        houseAndMaterialsButton.setOnClickListener(this);
        jobsAndOccupationsButton = (Button) findViewById(R.id.jobs_and_occupations_button);
        jobsAndOccupationsButton.setOnClickListener(this);
        measurementButton = (Button) findViewById(R.id.measurement_button);
        measurementButton.setOnClickListener(this);
        musicButton = (Button) findViewById(R.id.music_button);
        musicButton.setOnClickListener(this);
        randomButton = (Button) findViewById(R.id.random_button);
        randomButton.setOnClickListener(this);
        schoolButton = (Button) findViewById(R.id.school_button);
        schoolButton.setOnClickListener(this);
        scienceButton = (Button) findViewById(R.id.science_button);
        scienceButton.setOnClickListener(this);
        shapesButton = (Button) findViewById(R.id.shapes_button);
        shapesButton.setOnClickListener(this);
        sportsButton = (Button) findViewById(R.id.sports_button);
        sportsButton.setOnClickListener(this);
        toolsButton = (Button) findViewById(R.id.tools_button);
        toolsButton.setOnClickListener(this);
        transportationButton = (Button) findViewById(R.id.transportation_button);
        transportationButton.setOnClickListener(this);
        treesAndFlowersButton = (Button) findViewById(R.id.trees_and_flowers_button);
        treesAndFlowersButton.setOnClickListener(this);
        weatherButton = (Button) findViewById(R.id.weather_button);
        weatherButton.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        Button b = (Button)v;
        String buttonText = b.getText().toString();
        if (user == null) {
            return;
        }
        Intent myIntent;

        Log.d(TAG, "v.getTag " + buttonText.toLowerCase());

        myIntent = new Intent(MainActivity.this, GameActivity.class);
        myIntent.putExtra("category", buttonText.toLowerCase());

        startActivity(myIntent);








        switch (v.getId()) {
            case R.id.anatomy_button:
                Log.d(TAG, "test button text: " + anatomyButton.getText().toString().toLowerCase());
                myIntent = new Intent(MainActivity.this, GameActivity.class);
                myIntent.putExtra("category", anatomyButton.getText().toString().toLowerCase());
                startActivity(myIntent);
                break;
            case R.id.animals_button:
                Log.d(TAG, "catgory button text: " + animalsButton.getText().toString().toLowerCase());
                myIntent = new Intent(MainActivity.this, GameActivity.class);
                myIntent.putExtra("category", animalsButton.getText().toString().toLowerCase());
                startActivity(myIntent);
        }
    }
}
