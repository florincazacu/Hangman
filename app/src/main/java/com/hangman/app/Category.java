package com.hangman.app;

/**
 * Created by Florin on 27-05-2017.
 */

public class Category {

    private String categoryName;
    private String gsReference;

    public Category() {

    }

    public Category(String categoryName, String score) {
        this.categoryName = categoryName;
        this.gsReference = score;
    }

    public String getName() {
        return categoryName;
    }

    public String getGsReference() {
        return gsReference;
    }

    public void setGsReference(String gsReference) {
        this.gsReference = gsReference;
    }
}