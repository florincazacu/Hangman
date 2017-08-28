package com.hangman.app;

import android.app.Application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by Florin on 23-08-2017.
 */

public class FileUtils {

    private String path;
    private String selectedCategory;
    private Application application;
    private File categories;
    private File category;
    private String[] words; //?

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

    public String[] getWordsFromCategoryFile() {
        File inStream = new File(downloadCategory().toString());
        BufferedReader buffReader;
        String line;
        try {
            buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(inStream)));
            line = buffReader.readLine();
            words = line.split(";");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return words;
    }
}
