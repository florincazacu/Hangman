package com.hangman.app;

/**
 * Created by Florin on 30-03-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import static com.hangman.app.R.id.buttons_layout;

public class GameActivity extends MainActivity implements View.OnClickListener {

    private static final String TAG = "GameActivityTag";

    private String mUsername;
    private int missedWordsCount = 0;
    private String selectedCategory;
    private String gsKey;
    private String path;

    private GameUtils mGameUtils;
    private FirebaseUtils mFirebaseUtils;
    private FileUtils mFileUtils;

    private TextView lettersTextView; //?
    private TextView triesLeft;
    private TextView scoresTextView; //?
    private ImageView pictureContainer; //?
    private Score scores; //?
    private String[] words; //?
    private int[] missedLetterImg = new int[]{R.drawable.hangman_1st_miss, R.drawable.hangman_2nd_miss,
            R.drawable.hangman_3rd_miss, R.drawable.hangman_4th_miss, R.drawable.hangman_5th_miss, R.drawable.hangman_game_over};

//    private File localFile; //?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent i = getIntent();
        selectedCategory = i.getStringExtra("CATEGORY");
        gsKey = i.getStringExtra("GS_KEY");

        mFirebaseUtils = new FirebaseUtils(selectedCategory, gsKey);
        mFirebaseUtils.createScoresReference();
        mFileUtils = new FileUtils(selectedCategory, this.getApplication());
        mGameUtils = new GameUtils(getWordsFromCategoryFile());

        lettersTextView = (TextView) findViewById(R.id.lettersInputArea);
        pictureContainer = (ImageView) findViewById(R.id.picture_container);
        pictureContainer.setImageResource(R.drawable.hangman_start);
        triesLeft = (TextView) findViewById(R.id.remaining_lives);
        triesLeft.setText(getString(R.string.tries_left, mGameUtils.getTriesLeft()));
        scoresTextView = (TextView) findViewById(R.id.score_text_view);

        if (mFirebaseUtils.getFirebaseUser() != null) {
            mUsername = mFirebaseUtils.getFirebaseUser().getDisplayName();
        }

        showScore(mUsername);

        createButtons();

        mFileUtils.downloadCategory();
        getWordsFromCategoryFile();
        startGameActivity();


        mFirebaseUtils.getStorageReference().getFile(mFileUtils.downloadCategory()).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                lettersTextView.setText(mGameUtils.createWordUnderscores());
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


    private String[] getWordsFromCategoryFile() {
        File inStream = new File(mFileUtils.downloadCategory().toString());
        BufferedReader buffReader;
        String line;
        try {
            buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(inStream)));
            line = buffReader.readLine();
            words = line.split(";");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return words;
    }

    @Override
    public void onClick(View view) {
        view.setEnabled(false);

        if (mGameUtils.isLetterContainedInWord((char) view.getTag())) {
//        if (mGameUtils.isLetterContainedInWord((int) view.getTag())) {
            lettersTextView.setText(mGameUtils.replaceLetter());
            if (!lettersTextView.getText().toString().contains(String.valueOf('_'))) {
                mGameUtils.addGuessedWord();
                mGameUtils.resetTries();
                Toast.makeText(this, "Congratulations!", Toast.LENGTH_SHORT).show();
                scores.increaseScore();
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                Query query = reference.child("scores").orderByChild("username").equalTo(mUsername);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                            String key = nodeDataSnapshot.getKey();
                            path = "/" + dataSnapshot.getKey() + "/" + key;
                            HashMap<String, Object> result = new HashMap<>();
                            result.put("score", scores.getScore());
                            reference.child(path).updateChildren(result);
                            scoresTextView.setText(getString(R.string.player_score, scores.getScore()));
                            triesLeft.setText(getString(R.string.tries_left, mGameUtils.getTriesLeft()));
                        } else {
                            scoresTextView.setText(getString(R.string.player_score, scores.getScore()));
                            if (scores != null) {
                                scores.setScore(scores.getScore());
                            } else {
                                scores = new Score(mUsername, scores.getScore());
                            }
                            mFirebaseUtils.pushScoreToFirebase(scores);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, ">>> Error:" + "find onCancelled:" + databaseError);
                    }
                });
            }
        } else {
            pictureContainer.setImageResource(missedLetterImg[missedWordsCount]);
            mGameUtils.subtractOneTry();
            triesLeft.setText(getString(R.string.tries_left, mGameUtils.getTriesLeft()));
            if (mGameUtils.getTriesLeft() == 0) {
                Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show();
                lettersTextView.setText(mGameUtils.getWordToGuess());
                LinearLayout buttons_layout = (LinearLayout) findViewById(R.id.buttons_layout);
                for (int i = 0; i < buttons_layout.getChildCount(); i++) {
                    LinearLayout row = (LinearLayout) buttons_layout.getChildAt(i);
                    for (int j = 0; j < row.getChildCount(); j++) {
                        Button letter_button = (Button) row.getChildAt(j);
                        letter_button.setEnabled(false);
                    }
                }
                return;
            }
            missedWordsCount++;
        }
    }

    public void createButtons() {
        LinearLayout layout = (LinearLayout) findViewById(buttons_layout);
        for (int i = 0; i < 3; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setWeightSum(9);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, getResources().getDimensionPixelSize(R.dimen.letter_button_height));
            params.weight = 1;
            for (int j = 0; j < 9; j++) {
                if (j + (i * 9) < mGameUtils.getAlphabetLetters().length) {
                    Button btnTag = new Button(this);
                    btnTag.setLayoutParams(params);
                    btnTag.setText(String.valueOf(mGameUtils.getAlphabetLetters()[j + (i * 9)]));
                    btnTag.setTag(mGameUtils.getAlphabetLetters()[j + (i * 9)]);
//                    btnTag.setTag(j + (i * 9));
                    btnTag.setOnClickListener(this);
                    row.addView(btnTag);
                }
            }
            layout.addView(row);
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
            case R.id.try_again:
                mGameUtils.resetGame();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void clearButtons() {
        LinearLayout layout = (LinearLayout) findViewById(buttons_layout);
        layout.removeAllViews();
    }


    private void showScore(String username) {
        mUsername = username;
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("scores").orderByChild("username").equalTo(mUsername);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        scores = snap.getValue(Score.class);
                        scoresTextView.setText(getString(R.string.player_score, scores.getScore()));
                    }
                } else {
                    Log.e(TAG, "onDataChange: NO DATA");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void startGameActivity() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("scores").orderByChild("username").equalTo(mUsername);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        scores = snap.getValue(Score.class);
                        scoresTextView.setText(getString(R.string.player_score, scores.getScore()));
                    }
                } else {
                    Log.e(TAG, "onDataChange: NO DATA");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }
}
