package com.example.android.projectbooksapi;

/**
 * Created by arturoahernandez on 2/22/18.
 */

public class Book {

    private String mTitle, mAuthor;

    public Book(String mTitle, String mAuthor) {
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }
}
