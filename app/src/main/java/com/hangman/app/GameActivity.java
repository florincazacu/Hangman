package com.hangman.app;

/**
 * Created by Florin on 30-03-2017.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Random;

public class GameActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "GameActivity";

    private static final char[] ALPHABET_LETTERS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private String mUsername;
    private String wordToGuess;
    private char[] letters;
    private int score;
    private int tries = 6;

    private TextView lettersArea;
    private TextView triesLeft;
    private TextView scoresTextView;
    private ImageView pictureContainer;
    private Score scores;
    private static String[] words;

    private File localFile;
    public StorageReference textRef;

    HashMap<String, String> guessedLetters = new HashMap<>();
    HashMap<String, String> guessedWords = new HashMap<>();
    HashMap<String, String> wordsFromFile = new HashMap<>();

    private FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference scoresReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        createButtons();
        lettersArea = (TextView) findViewById(R.id.lettersInputArea);
        pictureContainer = (ImageView) findViewById(R.id.picture_container);
        pictureContainer.setImageResource(R.drawable.hangman_start);
        triesLeft = (TextView) findViewById(R.id.remaining_lives);
        triesLeft.setText(getString(R.string.tries_left, tries));
        scoresTextView = (TextView) findViewById(R.id.score_text_view);

//        Intent i = getIntent();
//        mUsername = i.getStringExtra("mUsername");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mUsername = user.getDisplayName();

        if (mFirebaseDatabase == null) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        scoresReference = mFirebaseDatabase.getReference().child("scores");
        scoresReference.keepSynced(true);

        File sdcard = Environment.getExternalStorageDirectory();

        if (isNetworkAvailable()) {
            try {
                File categories = new File(Environment.getExternalStorageDirectory().getPath());
                localFile = new File(categories, "test.txt");
            } catch (Exception e) {
                Log.d(TAG, "network available error: " + e.getMessage());
            }
        } else {
            try {
                localFile = new File(sdcard, "test.txt");
                File inStream = new File(localFile.toString());
                BufferedReader buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(inStream)));
                String line;
                line = buffReader.readLine();
                words = line.split("; ");
                for (String word : words) {
                    wordsFromFile.put(word, word);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        startGameActivity();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        textRef = storageReference.child("test/test.txt");

        if (localFile != null) {
            textRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    try {
                        File inStream = new File(localFile.toString());
                        BufferedReader buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(inStream)));
                        String line = buffReader.readLine();
                        words = line.split("; ");
                        for (String word : words) {
                            wordsFromFile.put(word, word);
                        }
                        wordToGuess = getWord();
                        letters = wordToGuess.toCharArray();
                        lettersArea.setText(createWordUnderscores());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Log.e(TAG, "onCancelled: " + exception.getMessage());
                }
            });
        }
        scores = new Score(mUsername, score);
        scoresTextView.setText(getString(R.string.player_score, score));
    }

    @Override
    public void onClick(View view) {
        if (isLetterContainedInWord(view)) {
            guessedLetters.put(Character.toString(ALPHABET_LETTERS[(int) view.getTag()]), Character.toString(ALPHABET_LETTERS[(int) view.getTag()]));
            replaceLetter();
            if (!lettersArea.getText().toString().contains(String.valueOf('_'))) {
                guessedWords.put(wordToGuess, wordToGuess);
                tries = 6;
                Toast.makeText(this, "Congratulations!", Toast.LENGTH_SHORT).show();
                score++;
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                Query query = reference.child("scores").orderByChild("username").equalTo(mUsername);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                            String key = nodeDataSnapshot.getKey();
                            String path = "/" + dataSnapshot.getKey() + "/" + key;
                            HashMap<String, Object> result = new HashMap<>();
                            result.put("score", score);
                            reference.child(path).updateChildren(result);
                            Log.d(TAG, "score case1 " + score);
                            scoresTextView.setText(getString(R.string.player_score, score));
                            triesLeft.setText(getString(R.string.tries_left, tries));
                        } else {
                            Log.d(TAG, "score case2 " + score);
                            scoresTextView.setText(getString(R.string.player_score, score));
                            if (scores != null) {
                                scores.setScore(score);
                            } else {
                                scores = new Score(mUsername, score);
                            }
                            scoresReference.push().setValue(scores);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, ">>> Error:" + "find onCancelled:" + databaseError);
                    }
                });
            }
        } else {
            tries--;
            switch (tries) {
                case 5:
                    pictureContainer.setImageResource(R.drawable.hangman_1st_miss);
                    triesLeft.setText(getString(R.string.tries_left, tries));
                    break;
                case 4:
                    pictureContainer.setImageResource(R.drawable.hangman_2nd_miss);
                    triesLeft.setText(getString(R.string.tries_left, tries));
                    break;
                case 3:
                    pictureContainer.setImageResource(R.drawable.hangman_3rd_miss);
                    triesLeft.setText(getString(R.string.tries_left, tries));
                    break;
                case 2:
                    pictureContainer.setImageResource(R.drawable.hangman_4th_miss);
                    triesLeft.setText(getString(R.string.tries_left, tries));
                    break;
                case 1:
                    pictureContainer.setImageResource(R.drawable.hangman_5th_miss);
                    triesLeft.setText(getString(R.string.tries_left, tries));
                    break;
                case 0:
                    pictureContainer.setImageResource(R.drawable.hangman_game_over);
                    triesLeft.setText(getString(R.string.tries_left, tries));
                    Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        view.setEnabled(false);
    }

    private boolean isLetterContainedInWord(View view) {
        try {
            for (char c : letters) {
                if (c == ALPHABET_LETTERS[(int) view.getTag()]) {
                    return true;
                }
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getWord() {
        Random randomWord = new Random();
        int index = randomWord.nextInt(words.length);
        return words[index];
    }

    public void replaceLetter() {
        StringBuffer guessedLettersStringBuffer = new StringBuffer();
        for (int i = 0; i < letters.length; i++) {
            if (guessedLetters.containsKey(String.valueOf(letters[i]))) {
                guessedLettersStringBuffer.append(Character.toString(letters[i])).append(' ');
            } else if (letters[i] == ' ') {
                guessedLettersStringBuffer.append(" / ");
            } else {
                guessedLettersStringBuffer.append("_ ");
            }
        }
        lettersArea.setText(guessedLettersStringBuffer);
    }

    public void createButtons() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.buttons_layout);
        for (int i = 0; i < 3; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
            for (int j = 0; j < 9; j++) {
                if (j + (i * 9) < ALPHABET_LETTERS.length) {
                    Button btnTag = new Button(this);
                    btnTag.setLayoutParams(new ActionBar.LayoutParams(140, 150));
                    btnTag.setText(String.valueOf(ALPHABET_LETTERS[j + (i * 9)]));
                    btnTag.setTag(j + (i * 9));
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
                pictureContainer.setImageResource(R.drawable.hangman_start);
                tries = 6;
                triesLeft.setText(getString(R.string.tries_left, tries));
                clearButtons();
                createButtons();
                getWord();
                for (String word : words) {
                    wordsFromFile.put(word, word);
                }
                wordToGuess = getWord();
                letters = wordToGuess.toCharArray();
                lettersArea.setText(createWordUnderscores());
                guessedLetters.clear();
                lettersArea.setText(createWordUnderscores());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void clearButtons() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.buttons_layout);
        layout.removeAllViews();
    }

    public StringBuffer createWordUnderscores() {
        StringBuffer underscores = new StringBuffer();
        for (int i = 0; i < letters.length; i++) {
            if (letters[i] == ' ') {
                underscores.append(" / ");
            } else {
                underscores.append("_ ");
            }
        }
        return underscores;
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
                        score = scores.getScore();
                        scoresTextView.setText(getString(R.string.player_score, score));
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
                        score = scores.getScore();
                        scoresTextView.setText(getString(R.string.player_score, score));
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
