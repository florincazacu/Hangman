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
    private String[] wordsTest = {"word1", "word2", "word3"};

    @Test
    public void verifyNoOfTriesLeftAtTheBeginningOfTheGame() throws Exception{
        GameUtils gameUtils = new GameUtils(new String[]{});
        assertEquals(6, gameUtils.getTriesLeft());
    }

    @Test
    public void verifyGetTriesLeft() {
        GameUtils gameUtils = new GameUtils(new String[]{});
        gameUtils.subtractOneTry();
        assertEquals(5, gameUtils.getTriesLeft());
        gameUtils.resetGame();
        assertEquals(6, gameUtils.getTriesLeft());
    }

    @Test
    public void verifySubtractOneTry() {
        GameUtils gameUtils = new GameUtils(new String[]{});
        gameUtils.subtractOneTry();
        assertEquals(5,gameUtils.getTriesLeft());
    }

    @Test
    public void verifyGetAlphabetLetters() {
        GameUtils gameUtils = new GameUtils(new String[]{});
        assertArrayEquals(ALPHABET_LETTERS, gameUtils.getAlphabetLetters());
    }

    @Test
    public void verifyGuessedListIsEmptyWhenTheGameStarts(){
        GameUtils gameUtils = new GameUtils(wordsTest);
        assertEquals(true, gameUtils.isGuessedWordsEmpty());
    }

    @Test
    public void verifyAddGuessedWord() {
        GameUtils gameUtils = new GameUtils(wordsTest);
        gameUtils.addGuessedWord();
        assertEquals(false, gameUtils.isGuessedWordsEmpty());
    }

    @Test
    public void verifyResetGame() {
        GameUtils gameUtils = new GameUtils(new String[]{});
        gameUtils.subtractOneTry();
        assertEquals(5, gameUtils.getTriesLeft());
        gameUtils.resetGame();
        assertEquals(6, gameUtils.getTriesLeft());
    }

    @Test
    public void verifyResetTries() {
        GameUtils gameUtils = new GameUtils(new String[]{});
        gameUtils.subtractOneTry();
        assertEquals(5, gameUtils.getTriesLeft());
        gameUtils.resetTries();
        assertEquals(6, gameUtils.getTriesLeft());
    }

    @Test
    public void verifyCreateWordUnderscores() {

    }

    @Test
    public void verifyIsLetterContainedInWord() {
        
    }
}
