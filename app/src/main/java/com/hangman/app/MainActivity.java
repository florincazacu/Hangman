package com.hangman.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivityTag";

    private TextView playerScoreTextView;
    private TextView highScoreTextView;
    private Button playButton;

    private FirebaseUtils mFirebaseUtils;

    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerScoreTextView = (TextView) findViewById(R.id.player_score_tv);
        highScoreTextView = (TextView) findViewById(R.id.high_score_tv);
        playButton = (Button) findViewById(R.id.play_button);
        playButton.setOnClickListener(this);

        mFirebaseUtils = new FirebaseUtils();

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mFirebaseUser == null) {
            Intent myIntent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(myIntent);
        }

        DatabaseReference mDatabasePlayers = FirebaseDatabase.getInstance().getReference();
        Query mDatabaseHighestPlayer = mDatabasePlayers.child("scores").orderByChild("score").limitToLast(1);
        mDatabaseHighestPlayer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    Score highScore = childSnapshot.getValue(Score.class);
                    highScoreTextView.setText(getString(R.string.high_score, highScore.getScore()));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException(); // don't swallow errors
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("scores").orderByChild("username").equalTo(mFirebaseUtils.getUsername());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        Score playerScore = childSnapshot.getValue(Score.class);
                        String userScore = playerScore.getUsername() + ": " + String.valueOf(playerScore.getScore());
                        playerScoreTextView.setText(userScore);
                    }
                } else {
                    Score playerScore = new Score(mFirebaseUtils.getUsername(), 0);
                    mFirebaseUtils.updateScoreInFirebase(playerScore);
                    String userScore = playerScore.getUsername() + ": " + String.valueOf(playerScore.getScore());
                    playerScoreTextView.setText(userScore);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, SelectCategoryActivity.class);
        startActivity(intent);
    }
}
