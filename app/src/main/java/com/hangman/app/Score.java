package com.hangman.app;

/**
 * Created by Florin on 30-03-2017.
 */

public class Score {

    private String username;
    private int score;

    public Score() {

    }

    public Score(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public String getUsername() {
        return username;
    }

    public void increaseScore() {
        score++;
    }
}
