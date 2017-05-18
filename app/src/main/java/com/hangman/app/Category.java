package com.hangman.app;

/**
 * Created by Florin on 17-05-2017.
 */

public class Category {

    private String gsReference;
    private String name;

    public Category(){

    }

    public Category(String gsReference, String name) {
        this.gsReference = gsReference;
        this.name = name;
    }

    public String getGsReference() {
        return gsReference;
    }

    public String getName() {
        return name;
    }
}
