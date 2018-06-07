package com.golendukhin.bookstoreinventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView bookTitleTextView = view.findViewById(R.id.book_title);
        TextView priceTextView = view.findViewById(R.id.price);
        TextView quantityTextView = view.findViewById(R.id.quantity);
        Button reduceQuantityButton = view.findViewById(R.id.reduce_quantity_button);

        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(BooksEntry._ID));
        String bookTitle = cursor.getString(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_BOOKS_PRODUCT_NAME));
        String price = context.getString(R.string.price_prefix) +
                String.valueOf(1.0 * cursor.getInt(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_BOOKS_PRICE)) / 100);
        final int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_BOOKS_QUANTITY));
        String quantityText = context.getString(R.string.quantity_prefix) + String.valueOf(quantity);

        bookTitleTextView.setText(bookTitle);
        priceTextView.setText(price);
        quantityTextView.setText(quantityText);
        reduceQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 0) {
                    int reducedQuantity = quantity - 1;
                    Uri uri = ContentUris.withAppendedId(BooksEntry.CONTENT_URI, id);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(BooksEntry.COLUMN_BOOKS_QUANTITY, reducedQuantity);
                    context.getContentResolver().update(uri, contentValues, null, null);
                } else {
                    Toast.makeText(context, context.getText(R.string.out_of_stock_toast), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}