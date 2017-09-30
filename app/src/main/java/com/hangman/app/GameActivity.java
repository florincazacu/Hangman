package com.hangman.app;

/**
 * Created by Florin on 30-03-2017.
 */

import android.content.Intent;
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

import static com.hangman.app.R.id.buttons_layout;

public class GameActivity extends MainActivity implements View.OnClickListener, GameContract.View {

    private static final String TAG = "GameActivityTag";


    private FileUtils mFileUtils;
    private GamePresenter mGamePresenter;
    private FirebaseUtils mFirebaseUtils;
    private Score playerScore;

    private TextView lettersTextView;
    private TextView triesLeftTextView;
    private TextView scoresTextView;
    private ImageView pictureContainer; //?


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent i = getIntent();
        String selectedCategory = i.getStringExtra("CATEGORY");
        String gsKey = i.getStringExtra("GS_KEY");

        mFirebaseUtils = new FirebaseUtils(selectedCategory, gsKey);
        mFirebaseUtils.createScoresReference();
        mFileUtils = new FileUtils(selectedCategory, this.getApplication());

        lettersTextView = (TextView) findViewById(R.id.lettersInputArea);
        triesLeftTextView = (TextView) findViewById(R.id.remaining_lives);
        scoresTextView = (TextView) findViewById(R.id.score_text_view);
        pictureContainer = (ImageView) findViewById(R.id.picture_container);

        mFirebaseUtils.getStorageReference().getFile(mFileUtils.downloadCategory()).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                mGamePresenter = new GamePresenter(GameActivity.this);
                mGamePresenter.addWords(mFileUtils.getWordsFromCategoryFile());
                mGamePresenter.startGame();
                mFileUtils.getWordsFromCategoryFile();
                pictureContainer.setImageResource(R.drawable.hangman_start);
                createButtons();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e(TAG, "onFailure: " + exception.getMessage());
                exception.printStackTrace();
            }
        });
        showScore();
    }

    public void clearButtons() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.buttons_layout);
        layout.removeAllViews();
    }

    @Override
    public void onClick(View view) {
        view.setEnabled(false);
        char selectedLetter = (char) view.getTag();
        mGamePresenter.selectLetter(selectedLetter);
    }

    public void createButtons() {
        LinearLayout layout = (LinearLayout) findViewById(buttons_layout);
        for (int i = 0; i < 3; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setWeightSum(9);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, getResources().getDimensionPixelSize(R.dimen.letter_button_height));
            params.weight = 1;
            for (int j = 0; j < 9; j++) {
                if (j + (i * 9) < AlphabetLetters.getAlphabetLetters().length) {
                    Button btnTag = new Button(this);
                    btnTag.setLayoutParams(params);
                    btnTag.setText(String.valueOf(AlphabetLetters.getAlphabetLetters()[j + (i * 9)]));
                    btnTag.setTag(AlphabetLetters.getAlphabetLetters()[j + (i * 9)]);
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
                mGamePresenter.tryAgain();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showScore() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("scores").orderByChild("username").equalTo(mFirebaseUtils.getUsername());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        playerScore = snap.getValue(Score.class);
                        scoresTextView.setText(getString(R.string.player_score, playerScore.getScore()));
                    }
                } else {
                    playerScore = new Score(mFirebaseUtils.getUsername(), 0);
                    mFirebaseUtils.updateScoreInFirebase(playerScore);
                    scoresTextView.setText(getString(R.string.player_score, playerScore.getScore()));
                    Log.e(TAG, "onDataChange: NO DATA");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    public void disableButtons() {
        LinearLayout buttons_layout = (LinearLayout) findViewById(R.id.buttons_layout);
        for (int i = 0; i < buttons_layout.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) buttons_layout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                Button letter_button = (Button) row.getChildAt(j);
                letter_button.setEnabled(false);
            }
        }
    }

    @Override
    public void displayLoadingIndicator(boolean display) {

    }

    @Override
    public void displayCongratulations() {
        Toast.makeText(GameActivity.this, "Congratulations!", Toast.LENGTH_SHORT).show();
        playerScore.increaseScore();
        mFirebaseUtils.updateScoreInFirebase(playerScore);
        scoresTextView.setText(getString(R.string.player_score, playerScore.getScore()));
        disableButtons();
    }

    @Override
    public void displayWrongLetterSelected(int resId, int triesLeft) {
        pictureContainer.setImageResource(resId);
        triesLeftTextView.setText(getString(R.string.tries_left, triesLeft));
    }

    @Override
    public void displayCorrectLetterSelected(StringBuffer letterToReplace) {
        lettersTextView.setText(letterToReplace);
    }

    @Override
    public void displayGameOver(String wordToGuess) {
        Toast.makeText(GameActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
        lettersTextView.setText(wordToGuess);
        disableButtons();
    }

    @Override
    public void displayGameStart(StringBuffer wordUnderscores, int defaultTries) {
        lettersTextView.setText(wordUnderscores);
        pictureContainer.setImageResource(R.drawable.hangman_start);
        triesLeftTextView.setText(getString(R.string.tries_left, defaultTries));
    }

    @Override
    public void displayOnTryAgain(StringBuffer wordUnderscores, int defaultTries) {
        clearButtons();
        createButtons();
        lettersTextView.setText(wordUnderscores);
        pictureContainer.setImageResource(R.drawable.hangman_start);
        triesLeftTextView.setText(getString(R.string.tries_left, defaultTries));
    }
}
