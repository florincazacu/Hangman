package com.hangman.app;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by Florin on 11-09-2017.
 */

public class GameUtilsTest {

    private final char[] ALPHABET_LETTERS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private HashMap<String, String> guessedWords = new HashMap<>();
//    private String[] wordsTest = {"word1", "word2", "word3"};
    private String[] wordsTest = {"word1", "word1"};
    private GameUtils gameUtils = new GameUtils(wordsTest);

    @Test
    public void verifyNoOfTriesLeftAtTheBeginningOfTheGame() throws Exception{
        assertEquals(6, gameUtils.getTriesLeft());
    }

    @Test
    public void verifyGetTriesLeft() {
        gameUtils.subtractOneTry();
        assertEquals(5, gameUtils.getTriesLeft());
        gameUtils.resetGame();
        assertEquals(6, gameUtils.getTriesLeft());
    }

    @Test
    public void verifyGenerateWordToGuess() {
        gameUtils.addGuessedWord();
        assertEquals(false, gameUtils.isGuessedWordsEmpty());
        gameUtils.generateWordToGuess();
        gameUtils.generateWordToGuess();
    }

    @Test
    public void verifySubtractOneTry() {
        gameUtils.subtractOneTry();
        assertEquals(5,gameUtils.getTriesLeft());
    }

    @Test
    public void verifyGetAlphabetLetters() {
        assertArrayEquals(ALPHABET_LETTERS, gameUtils.getAlphabetLetters());
    }

    @Test
    public void verifyGuessedListIsEmptyWhenTheGameStarts(){
        assertEquals(true, gameUtils.isGuessedWordsEmpty());
    }

    @Test
    public void verifyAddGuessedWord() {
        gameUtils.addGuessedWord();
        assertEquals(false, gameUtils.isGuessedWordsEmpty());
    }

    @Test
    public void verifyResetGame() {
        gameUtils.subtractOneTry();
        assertEquals(5, gameUtils.getTriesLeft());
        gameUtils.resetGame();
        assertEquals(6, gameUtils.getTriesLeft());
    }

    @Test
    public void verifyResetTries() {
        gameUtils.subtractOneTry();
        assertEquals(5, gameUtils.getTriesLeft());
        gameUtils.resetTries();
        assertEquals(6, gameUtils.getTriesLeft());
    }

    @Test
    public void verifyCreateWordUnderscores() {
        GameUtils gameUtils = new GameUtils(new String[]{});

    }

    @Test
    public void verifyIsLetterContainedInWord() {
        gameUtils.createWordUnderscores();
        assertEquals(false, gameUtils.isLetterContainedInWord('a'));
        assertEquals(true, gameUtils.isLetterContainedInWord('w'));
    }
}
