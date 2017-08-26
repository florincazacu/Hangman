package com.hangman.app;

import android.app.Application;

import java.io.File;

/**
 * Created by Florin on 23-08-2017.
 */

public class FileUtils {

    private String path;
    private String selectedCategory;
    private Application application;
    private File categories;
    private File category;

    public FileUtils(String category, Application application) {
        this.application = application;
        this.selectedCategory = category;
    }

    private String getPath() {
        return path = "categories/" + selectedCategory + ".txt";
    }

    public File downloadCategory() {
        File categoryFile = new File(application.getFilesDir(), "categories");
        categoryFile.mkdir();
        category = new File(categoryFile, selectedCategory + ".txt");
        return category;
    }
}
