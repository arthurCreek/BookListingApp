package com.example.android.projectbooksapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by arturoahernandez on 2/22/18.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    //Constructor here
    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }


    //Overide the getView method
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_list_view, parent, false);
        }

        //Find the Book at the given position in the list of Books
        Book currentBook = getItem(position);

        //Get the text view of the listItemView and set title text from the currentBook
        TextView bookTitle = listItemView.findViewById(R.id.title);
        bookTitle.setText(currentBook.getmTitle());

        //Get the text view of the listItemView and set the author from the currentBook
        TextView bookAuthor = listItemView.findViewById(R.id.author);
        bookAuthor.setText(currentBook.getmAuthor());



        return listItemView;
    }
}
