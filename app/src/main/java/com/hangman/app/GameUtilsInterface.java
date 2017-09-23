package com.hangman.app;

/**
 * Created by Florin on 23-09-2017.
 */

public interface GameUtilsInterface {

    void onGuessedWord();
    void onWrongLetterSelected();
    void onCorrectLetterSelected();
    void onGameOver();
}
