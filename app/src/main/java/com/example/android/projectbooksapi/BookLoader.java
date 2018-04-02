package com.example.android.projectbooksapi;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by arturoahernandez on 2/22/18.
 */

public class BookLoader extends AsyncTaskLoader {

    /** Tag for log messages */
    private static final String LOG_TAG = BookLoader.class.getName();

    /** Query URL */
    private String mUrl;

    public BookLoader(Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        if(mUrl == null){
            return null;
        }

        ArrayList<Book> books = QueryUtils.extractBooks(mUrl);
        return books;
    }
}
