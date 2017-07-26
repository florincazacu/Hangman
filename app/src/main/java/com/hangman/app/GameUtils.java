package com.hangman.app;

import android.view.View;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Florin on 19-07-2017.
 */

class GameUtils {

    private int livesLeft;
    private String[] words;
    private String wordToGuess;
    private char[] letters;
    private int score;

    HashMap<String, String> guessedLetters = new HashMap<>();
    HashMap<String, String> guessedWords = new HashMap<>();

    public GameUtils(String[] words) {
        this.words = words;
    }

    public void increaseScore() {
        score++;
    }

    public String generateWordToGuess() {
        Random randomWord = new Random();
        int index = randomWord.nextInt(words.length);
        return words[index];
    }

    String getWord() {
        Random randomWord = new Random();
        int index = randomWord.nextInt(words.length);
        return words[index];
    }

    void convertWordToUnderscores() {
        wordToGuess = getWord();
        letters = wordToGuess.toCharArray();
    }

    public StringBuffer createWordUnderscores() {
        convertWordToUnderscores();
        StringBuffer underscores = new StringBuffer();
        for (int i = 0; i < letters.length; i++) {
            if (letters[i] == ' ') {
                underscores.append(" / ");
            } else if (letters[i] == '\'') {
                underscores.append(" ' ");
            } else {
                underscores.append("_ ");
            }
        }
        return underscores;
    }

    boolean isLetterContainedInWord(View view, char[] alphabet_letters) {
        try {
            for (char c : letters) {
                if (c == alphabet_letters[(int) view.getTag()]) {
                    guessedLetters.put(Character.toString(alphabet_letters[(int) view.getTag()]), Character.toString(alphabet_letters[(int) view.getTag()]));
                    return true;
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public StringBuffer replaceLetter() {
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
        return guessedLettersStringBuffer;
    }

    public void addGuessedWord() {
        guessedWords.put(wordToGuess, wordToGuess);
    }

    public void startGame() {

    }

    public String getWordToGuess() {
        return wordToGuess;
    }

    public void resetGame() {

    }
}
