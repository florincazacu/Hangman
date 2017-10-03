package com.hangman.app;

import android.app.Application;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by Florin on 23-08-2017.
 */

public class FileUtils {

    private static final String TAG = "FileUtilsTag";

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
        BufferedReader buffReader;
        String line;
        try {
            buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(downloadCategory())));
            line = buffReader.readLine();
            words = line.split(";");
        } catch (Exception e) {
            Log.e(TAG, "getWordsFromCategoryFile crash " + e);
        }
        return words;
    }

}
