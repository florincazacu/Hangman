package com.hangman.app;

/**
 * Created by Florin on 19-07-2017.
 */

public class MainPresenter implements MainContract.UserActionsListener {


    private MainContract.View view;

    public MainPresenter(MainContract.View view)
    {

        this.view = view;
    }


    @Override
    public void selectLetter(String letter) {

    }
}
