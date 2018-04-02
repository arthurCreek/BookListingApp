package com.example.android.projectbooksapi;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by arturoahernandez on 2/22/18.
 */

public class QueryUtils {

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing a JSON response.
     */

    public static ArrayList<Book> extractBooks(String urlBooks) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(urlBooks)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Book> books = new ArrayList<Book>();

        //Use the createURL helper class to convert string into a URL
        URL url = createURL(urlBooks);

        String jsonResponse = "";

        //Try making a Http request using the makeHttpRequest helper
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            // TODO Handle the IOException
            Log.e(LOG_TAG, "Error making HTTP Request");
        }


        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        if (jsonResponse != null) {
            try {

                // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
                // build up a list of Earthquake objects with the corresponding data.
                JSONObject root = new JSONObject(jsonResponse);

                // Make sure that there are actually search results or else you get a JSON error
                int itemTotal = root.getInt("totalItems");
                if(itemTotal == 0){
                    return books;
                }

                // Store items array
                JSONArray bookItems = root.getJSONArray("items");

                // Go through items array and store more values
                for (int i = 0; i < bookItems.length(); i++) {
                    JSONObject currentBook = bookItems.getJSONObject(i);

                    JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                    String bookTitle = volumeInfo.getString("title");
                    JSONArray bookAuthorArray;

                    // Create a string builder and then convert with toString
                    StringBuilder outputAuthors = new StringBuilder();

                    if((bookAuthorArray = volumeInfo.optJSONArray("authors")) == null){
                        outputAuthors.append("No authors available");
                    } else {
                        for (int authorLength = 0; authorLength < bookAuthorArray.length(); authorLength++){
                            outputAuthors.append(bookAuthorArray.getString(authorLength));
                        }
                    }

                    String bookAuthors = outputAuthors.toString();

                    // Create the new Book wit information given
                    Book book = new Book(bookTitle, bookAuthors);
                    books.add(book);
                }

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
            }
        } else {
            return null;
        }
        return books;
    }

    // Create and validate the url here
    private static URL createURL(String urlString){
        URL url = null;

        try{
            url = new URL(urlString);
        } catch (MalformedURLException exception){
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }

        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                //Get the inputStream information by sing the readFromStream helper
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            // TODO: Handle the exception
            Log.e(LOG_TAG, "Error response code " + urlConnection.getResponseCode());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }
}
