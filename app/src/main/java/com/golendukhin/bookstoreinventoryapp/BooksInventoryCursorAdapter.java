package com.golendukhin.bookstoreinventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.golendukhin.bookstoreinventoryapp.data_base.BooksInventoryContract.BooksEntry;

public class BooksInventoryCursorAdapter extends CursorAdapter {

    public BooksInventoryCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView bookTitleTextView = view.findViewById(R.id.book_title);
        TextView priceTextView = view.findViewById(R.id.price);
        TextView quantityTextView = view.findViewById(R.id.quantity);

        String bookTitle = cursor.getString(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_BOOKS_PRODUCT_NAME));
        String price = "price: $" + String.valueOf(1.0 * cursor.getInt(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_BOOKS_PRICE)) / 100);
        String quantity = "quantity: " + String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_BOOKS_PRICE)));

        bookTitleTextView.setText(bookTitle);
        priceTextView.setText(price);
        quantityTextView.setText(quantity);
    }
}
