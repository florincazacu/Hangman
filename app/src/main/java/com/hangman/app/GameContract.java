package com.hangman.app;

/**
 * Created by Florin on 26-09-2017.
 */

public class GameContract {

    public interface UserActionsListener {

        void onGuessedWord();
        void onWrongLetterSelected();
        void onCorrectLetterSelected();
        void onGameOver();
        void onStartGame();
        void onTryAgain();
    }

    public interface View{

        void displayLoadingIndicator(boolean display);
        void displayCongratulations();
        void displayWrongLetterSelected(int missedLetterCount, int triesLeft);
        void displayCorrectLetterSelected(StringBuffer letterToReplace);
        void displayGameOver(String wordToGuess);
        void displayGameStart(StringBuffer wordUnderscores, int triesLeft);
        void displayOnTryAgain(StringBuffer wordUnderscores, int triesLeft);
    }

}
