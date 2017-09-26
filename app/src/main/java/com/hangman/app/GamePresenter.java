package com.hangman.app;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Florin on 19-07-2017.
 */

class GamePresenter implements GameContract.UserActionsListener {

    private static final int DEFAULT_MISSED_LETTERS_COUNT = 0;
    private final char[] ALPHABET_LETTERS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static final int DEFAULT_NUMBER_OF_TRIES = 6;
    private static final String TAG = "GamePresenter";

    private GameContract.View mView;

    private int triesLeft = 6;
    private String[] words;
    private String wordToGuess;
    private char[] letters;
    private int missedLettersCount = 0;
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
        startGame();
    }

    public void tryAgain() {
        generateWordToGuess();
        guessedLetters.clear();
        underscores.setLength(0);
        resetTries();
        resetMissedLettersCount();
        mView.displayOnTryAgain(convertWordToUnderscores(), DEFAULT_NUMBER_OF_TRIES);
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
        if (missedLettersCount == 5) {
            underscores.setLength(0);
            mView.displayGameOver(getWordToGuess());
        }
    }

    public boolean isWordGuessed() {
        if (!underscores.toString().contains(String.valueOf('_'))) {
            return true;
        }
        return false;
    }

    public void resetMissedLettersCount() {
        missedLettersCount = DEFAULT_MISSED_LETTERS_COUNT;
    }

    @Override
    public void selectLetter(char selectedLetter) {
        if (isLetterContainedInWord(selectedLetter)) {
            replaceLetter();
            onCorrectLetterSelected();
            if (isWordGuessed()) {
                addGuessedWord();
                resetTries();
                mView.displayCongratulations();
            }
        } else {
            subtractOneTry();
            increaseMissedLettersCount();
            if (triesLeft == 0) {
                resetMissedLettersCount();
                mView.displayGameOver(getWordToGuess());
            } else {
                mView.displayWrongLetterSelected(getMissedLettersCount(),getTriesLeft());
            }
        }
    }

    @Override
    public void onCorrectLetterSelected() {
        mView.displayCorrectLetterSelected(replaceLetter());
    }

    @Override
    public void startGame() {
        mView.displayGameStart(convertWordToUnderscores(), DEFAULT_NUMBER_OF_TRIES);

    }
}
