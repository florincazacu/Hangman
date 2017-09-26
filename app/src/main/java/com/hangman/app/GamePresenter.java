package com.hangman.app;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Florin on 19-07-2017.
 */

class GamePresenter {

    private final char[] ALPHABET_LETTERS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static final int DEFAULT_NUMBER_OF_TRIES = 6;
    private static final String TAG = "GamePresenter";

    private UserActionsInterface userActionsInterfaceListener;
    private GameViewInterface gameViewInterfaceListener;

    private int triesLeft = 6;
    private String[] words;
    private String wordToGuess;
    private char[] letters;
    private int missedLettersCount = 0;
    private StringBuffer underscores = new StringBuffer();

    private HashMap<String, String> guessedLetters = new HashMap<>();
    private HashMap<String, String> guessedWords = new HashMap<>();

    GamePresenter(String[] words) {
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

    public String generateWordToGuess() {
        Random randomWord = new Random();
        int index = randomWord.nextInt(words.length);
        while (guessedWords.containsValue(words[index])) {
            index = randomWord.nextInt(words.length);
        }
        return words[index];
    }

    public int getTriesLeft() {
        return triesLeft;
    }

    public void subtractOneTry() {
            triesLeft--;
    }

    public StringBuffer convertWordToUnderscores() {
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

    public boolean isLetterContainedInWord(char selectedLetter) {
        if (ArrayUtils.contains(letters, selectedLetter)) {
            guessedLetters.put(Character.toString(selectedLetter), Character.toString(selectedLetter));
            return true;
        } else {
            return false;
        }
    }

    public void startNewGame() {
        userActionsInterfaceListener.onStartGame(convertWordToUnderscores(), getTriesLeft());
    }

    public void tryAgain() {
        generateWordToGuess();
        userActionsInterfaceListener.onTryAgain(convertWordToUnderscores(), DEFAULT_NUMBER_OF_TRIES);
    }

    public void verifySelectedLetter(char selectedLetter) {
        if (isLetterContainedInWord(selectedLetter)) {
            replaceLetter();
            userActionsInterfaceListener.onCorrectLetterSelected(replaceLetter());
            if (isWordGuessed()) {
                addGuessedWord();
                resetTries();
                gameViewInterfaceListener.displayCongratulations();
                userActionsInterfaceListener.onGuessedWord();
            }
        } else {
            subtractOneTry();
            increaseMissedLettersCount();
            if (triesLeft == 0) {
                gameViewInterfaceListener.displayGameOver();
                userActionsInterfaceListener.onGameOver(wordToGuess);
            } else {
                userActionsInterfaceListener.onWrongLetterSelected(getMissedLettersCount(),getTriesLeft());
            }
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
        underscores = guessedLettersStringBuffer;
        return underscores;
    }

    public void addGuessedWord() {
        guessedWords.put(wordToGuess, wordToGuess);
    }

    public boolean isGuessedWordsEmpty() {
        return guessedWords.isEmpty();
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

    public int getMissedLettersCount() {
        return missedLettersCount;
    }

    public void increaseMissedLettersCount() {
        missedLettersCount++;
    }

    public boolean isWordGuessed() {
        if (!underscores.toString().contains(String.valueOf('_'))) {
            return true;
        }
        return false;
    }

    public void setUserActionsInterfaceListener(UserActionsInterface userActionsInterfaceListener) {
        this.userActionsInterfaceListener = userActionsInterfaceListener;
    }

    public void setGameViewInterfaceListener(GameViewInterface gameViewInterfaceListener) {
        this.gameViewInterfaceListener = gameViewInterfaceListener;
    }
}
