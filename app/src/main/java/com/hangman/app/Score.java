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

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void increaseScore() {
        score++;
    }
}
