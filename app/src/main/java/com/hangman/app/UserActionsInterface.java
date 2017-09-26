package com.hangman.app;

/**
 * Created by Florin on 23-09-2017.
 */

public interface UserActionsInterface {

    void onGuessedWord();
    void onWrongLetterSelected(int missedLetterCount, int triesLeft);
    void onCorrectLetterSelected(StringBuffer letterToReplace );
    void onGameOver(String wordToGuess);
    void onStartGame(StringBuffer wordUnderscores, int triesLeft);
    void onTryAgain(StringBuffer wordUnderscores, int triesLeft);
}
