package com.example.android.projectbooksapi;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class BooksActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Book>>{

    //Create necessary values here
    private String googleAPI = "https://www.googleapis.com/books/v1/volumes?q=";
    private String maxResults = "&maxResults=20";
    private static final int LOADER_INT_ID = 1;
    private String mURLFinal = "";
    private TextView mEmptyStateTextView;
    private BookAdapter mAdapter;
    private View loadingIndicator;
    private Boolean isRunning = false;
    private LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        //make a LoaderManager
        loaderManager = getLoaderManager();

        //Give user clear instruction to enter search terms
        mEmptyStateTextView = findViewById(R.id.empty_view);
        mEmptyStateTextView.setText(R.string.enter_search);
        mEmptyStateTextView.setVisibility(View.VISIBLE);


        //Here the user will be able to search books based on what they type in
        final EditText editText = findViewById(R.id.book_search);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                //Create a connectivity manager and store network info
                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

                //Get the text from the edit text field
                String editorText = editText.getText().toString();

                //Create the url query based on what the user typed in
                mURLFinal = googleAPI+editorText+maxResults;
                boolean handled = false;
                //Continue only if action search is activated && editor text is !empty
                if (i == EditorInfo.IME_ACTION_SEARCH && !editorText.matches("")) {

                    mEmptyStateTextView.setVisibility(View.GONE);

                    if(networkInfo != null && networkInfo.isConnected()){
                        loadingIndicator = findViewById(R.id.loading_spinner);
                        loadingIndicator.setVisibility(View.VISIBLE);
                        if(isRunning){
                            loaderManager.restartLoader(LOADER_INT_ID, null, BooksActivity.this);
                        }
                        loaderManager.initLoader(LOADER_INT_ID, null, BooksActivity.this);
                    } else {
                        //If no internet connection then state it here
                        View loadingIndicator = findViewById(R.id.loading_spinner);
                        loadingIndicator.setVisibility(View.GONE);

                        mEmptyStateTextView = findViewById(R.id.empty_view);
                        // Clear the adapter here
                        if (mAdapter != null) {
                            mAdapter.clear();
                        }
                        mEmptyStateTextView.setText(R.string.no_internet);
                        mEmptyStateTextView.setVisibility(View.VISIBLE);

                    }
                    handled = true;
                } else {
                    //Just in case the user hits search and did not type anything show no books
                    mEmptyStateTextView = findViewById(R.id.empty_view);
                    if (networkInfo != null && networkInfo.isConnected()){
                        // Clear the adapter here
                        if (mAdapter != null) {
                            mAdapter.clear();
                        }
                        mEmptyStateTextView.setText(R.string.no_books);
                    } else {
                        // Clear the adapter here
                        if (mAdapter != null) {
                            mAdapter.clear();
                        }
                        mEmptyStateTextView.setText(R.string.no_internet);
                    }

                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                }
                return handled;
            }
        });
    }

    //Create te onCreateLoader here and pass in Bundle
    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int i, Bundle urls) {
        isRunning = true;
        return new BookLoader(BooksActivity.this, mURLFinal);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {

        // Hide loading indicator because the data has been loaded
        loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);

        // Clear the adapter here
        if (mAdapter != null) {
            mAdapter.clear();
        }

        //Make sure books is not empty or null
        if (books.isEmpty() || books == null){
            mEmptyStateTextView = findViewById(R.id.empty_view);
            mEmptyStateTextView.setText(R.string.no_books);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        } else {
            updateUI(books);
        }
    }

    // Clear the adapter here onLoaderReset
    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        if (mAdapter != null) {
            mAdapter.clear();
        }
    }

    private void updateUI(ArrayList<Book> books){
        //Create a listView from id list
        ListView listView = findViewById(R.id.list);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);

        //Create adapter instance for BookAdapter
        mAdapter = new BookAdapter(BooksActivity.this, books);

        //Set the BookAdapter on the ListView
        listView.setAdapter(mAdapter);
    }
}
