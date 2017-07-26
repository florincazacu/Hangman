package com.hangman.app;

/**
 * Created by Florin on 19-07-2017.
 */

public class MainContract {
    public interface View {

        public void updateLetters();
    }

    public interface UserActionsListener{
        public void selectLetter(String letter);

    }
}
