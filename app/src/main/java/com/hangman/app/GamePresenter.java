package com.hangman.app;

import android.util.Log;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Florin on 19-07-2017.
 */

class GamePresenter implements GameContract.UserActionsListener {

    private static final int DEFAULT_NUMBER_OF_TRIES = 5;
    private static final String TAG = "GamePresenter";

    private int[] missedLetterImg = new int[]{R.drawable.hangman_1st_miss, R.drawable.hangman_2nd_miss,
            R.drawable.hangman_3rd_miss, R.drawable.hangman_4th_miss, R.drawable.hangman_5th_miss, R.drawable.hangman_game_over};

    private GameContract.View mView;

    private int triesLeft = DEFAULT_NUMBER_OF_TRIES;
    private String[] words;
    private String wordToGuess;
    private char[] letters;
    private StringBuffer underscores = new StringBuffer();

    private HashMap<String, String> guessedLetters = new HashMap<>();
    private HashMap<String, String> guessedWords = new HashMap<>();

    public GamePresenter(GameContract.View view) {
        mView = view;
    }

    public void addWords(final String[] words) {
        if (words != null) {
            this.words = words;
        } else {
            throw new NullPointerException("The array is empty!");
        }
    }

    private void convertWordToCharArray() {
        wordToGuess = generateWordToGuess();
        letters = wordToGuess.toCharArray();
    }

    private String generateWordToGuess() {
        Random randomWord = new Random();
        int index = randomWord.nextInt(words.length);
        while (guessedWords.containsValue(words[index])) {
            index = randomWord.nextInt(words.length);
        }
        return words[index];
    }

    private int getTriesLeft() {
        return triesLeft;
    }

    public boolean isGameOver() {
        if (triesLeft == 0) {
            return true;
        } else {
            return false;
        }
    }

    private void subtractOneTry() {
        triesLeft--;
    }

    private StringBuffer convertWordToUnderscores() {
        underscores.setLength(0);
        convertWordToCharArray();
        for (char letter : letters) {
            if (letter == ' ') {
                underscores.append(" / ");
            } else if (letter == '\'') {
                underscores.append(" ' ");
            } else {
                underscores.append("_ ");
            }
        }
        return underscores;
    }

    private boolean isLetterContainedInWord(char selectedLetter) {
        if (ArrayUtils.contains(letters, selectedLetter)) {
            return true;
        } else {
            return false;
        }
    }

    private StringBuffer replaceLetter() {
        StringBuffer guessedLettersStringBuffer = new StringBuffer();
        for (char letter : letters) {
            if (guessedLetters.containsKey(String.valueOf(letter))) {
                guessedLettersStringBuffer.append(Character.toString(letter)).append(' ');
            } else if (letter == ' ') {
                guessedLettersStringBuffer.append(" / ");
            } else if (letter == '\'') {
                guessedLettersStringBuffer.append(" ' ");
            } else {
                guessedLettersStringBuffer.append("_ ");
            }
        }
        underscores = guessedLettersStringBuffer;
        return underscores;
    }

    private void addGuessedWord() {
        guessedWords.put(wordToGuess, wordToGuess);
    }

    private String getWordToGuess() {
        return wordToGuess;
    }

    private void resetTries() {
        triesLeft = DEFAULT_NUMBER_OF_TRIES;
    }

    private boolean isWordGuessed() {
        if (!underscores.toString().contains(String.valueOf('_'))) {
            return true;
        }
        return false;
    }

    @Override
    public void startGame() {
        mView.displayGameStart(convertWordToUnderscores(), DEFAULT_NUMBER_OF_TRIES);
    }

    private void addGuessedLetter(char selectedLetter) {
        guessedLetters.put(Character.toString(selectedLetter), Character.toString(selectedLetter));
    }

    private int getResId() {
        return missedLetterImg[DEFAULT_NUMBER_OF_TRIES - getTriesLeft()];
    }

    @Override
    public void selectLetter(char selectedLetter) {
        checkSelectedLetter(selectedLetter);
        if (isGameOver()) {
            mView.displayGameOver(getWordToGuess());
        } else if (isWordGuessed()) {
            addGuessedWord();
            mView.displayCongratulations();
        }
    }

    private void checkSelectedLetter(char selectedLetter) {
        if (isLetterContainedInWord(selectedLetter)) {
            addGuessedLetter(selectedLetter);
            mView.displayCorrectLetterSelected(replaceLetter());
        } else {
            subtractOneTry();
            mView.displayWrongLetterSelected(getResId(), getTriesLeft());
        }
    }

    public void tryAgain() {
        generateWordToGuess();
        guessedLetters.clear();
        underscores.setLength(0);
        resetTries();
        mView.displayOnTryAgain(convertWordToUnderscores(), DEFAULT_NUMBER_OF_TRIES);
    }
}
