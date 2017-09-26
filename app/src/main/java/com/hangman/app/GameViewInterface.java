package com.hangman.app;

/**
 * Created by Florin on 26-09-2017.
 */

public interface GameViewInterface {

    void displayLoadingIndicator(boolean display);
    void displayCongratulations();
    void displayGameOver();
}
