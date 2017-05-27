package com.hangman.app;

/**
 * Created by Florin on 27-05-2017.
 */

public class Category {

    private String name;
    private String gs_reference;

    public Category() {

    }

    public Category(String gs_reference, String name) {
        this.name = name;
        this.gs_reference = gs_reference;
    }

    public String getName() {
        return name;
    }

    public String getGs_reference() {
        return gs_reference;
    }

    public void setGs_reference(String gs_reference) {
        this.gs_reference = gs_reference;
    }
}