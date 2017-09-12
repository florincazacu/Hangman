package com.hangman.app;

import android.util.Log;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Florin on 19-07-2017.
 */

class GameUtils {

    private final char[] ALPHABET_LETTERS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static final int DEFAULT_NUMBER_OF_TRIES = 6;
    private static final String TAG = "GameUtils";
    private int triesLeft = 6;
    private String[] words;
    private String wordToGuess;
    private char[] letters;

    private HashMap<String, String> guessedLetters = new HashMap<>();
    private HashMap<String, String> guessedWords = new HashMap<>();

    GameUtils(String[] words) {
        if (words != null) {
            this.words = words;
        } else {
            throw new NullPointerException("The array is empty!");
        }
    }

    private String generateWordToGuess() {
        Random randomWord = new Random();
        int index = randomWord.nextInt(words.length-1);
        return words[index];
    }

    private void convertWordToUnderscores() {
        wordToGuess = generateWordToGuess();
        letters = wordToGuess.toCharArray();
    }

    public int getTriesLeft() {
        return triesLeft;
    }

    public void subtractOneTry() {
        triesLeft--;
    }

    StringBuffer createWordUnderscores() {
        convertWordToUnderscores();
        StringBuffer underscores = new StringBuffer();
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

    boolean isLetterContainedInWord(char selectedLetter) {
        if (ArrayUtils.contains(letters, selectedLetter)) {
            guessedLetters.put(Character.toString(selectedLetter), Character.toString(selectedLetter));
            return true;
        } else {
            return false;
        }
    }

    public char[] getAlphabetLetters() {
        return ALPHABET_LETTERS;
    }

    public StringBuffer replaceLetter() {
        StringBuffer guessedLettersStringBuffer = new StringBuffer();
        for (char letter : letters) {
            Log.d(TAG, "letter " + String.valueOf(letter));
            if (guessedLetters.containsKey(String.valueOf(letter))) {
                Log.d(TAG, "guessedLetters.containsKey " + String.valueOf(letter));
                guessedLettersStringBuffer.append(Character.toString(letter)).append(' ');
            } else if (letter == ' ') {
                guessedLettersStringBuffer.append(" / ");
            } else if (letter == '\'') {
                guessedLettersStringBuffer.append(" ' ");
            } else {
                guessedLettersStringBuffer.append("_ ");
            }
        }
        Log.d(TAG, "replaceLetter " + guessedLettersStringBuffer);
        return guessedLettersStringBuffer;
    }

    public void addGuessedWord() {
        guessedWords.put(wordToGuess, wordToGuess);
    }

    public boolean isGuessedWordsEmpty(){
        return guessedWords.isEmpty();
    }

    public void startGame() {

    }

    public String getWordToGuess() {
        return wordToGuess;
    }

    public void resetGame() {
        resetTries();
    }

    public void resetTries() {
        triesLeft = DEFAULT_NUMBER_OF_TRIES;
    }
}
