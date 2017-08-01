package com.hangman.app;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Florin on 19-07-2017.
 */

class GameUtils {

    private final char[] ALPHABET_LETTERS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private int triesLeft;
    private String[] words;
    private String wordToGuess;
    private char[] letters;

    private HashMap<String, String> guessedLetters = new HashMap<>();
    private HashMap<String, String> guessedWords = new HashMap<>();

    GameUtils(String[] words) {
        this.words = words;
    }

    private String generateWordToGuess() {
        Random randomWord = new Random();
        int index = randomWord.nextInt(words.length);
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

    boolean isLetterContainedInWord(char selectedLetter) {
        if (ArrayUtils.contains(letters, selectedLetter)) {
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

    public void resetTries() {
        triesLeft = 6;
    }
}
