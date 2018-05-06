package com.golendukhin.bookstoreinventoryapp.data_base;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public final class DataBaseUtils {

    public static void insertData(Context context) {
        BooksInventoryDBHelper booksInventoryDBHelper = new BooksInventoryDBHelper(context);
        SQLiteDatabase sqLiteDatabase = booksInventoryDBHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_PRODUCT_NAME, "Jerome K. Jerome. Three Men in a Boat (To say Nothing Of The Dog");
        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_PRICE, 599);
        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_QUANTITY, 35);
        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_SUPPLIER_NAME, "Books LTD.");
        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE_NUMBER, "999-580-56-12");
        sqLiteDatabase.insert(BooksInventoryContract.BooksEntry.TABLE_NAME, null, contentValues);
        contentValues.clear();

        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_PRODUCT_NAME, "Robinson Crusoe by Daniel Defoe");
        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_PRICE, 255);
        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_QUANTITY, 41);
        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_SUPPLIER_NAME, "English Literature Publisher");
        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE_NUMBER, "569-956-21-00");
        sqLiteDatabase.insert(BooksInventoryContract.BooksEntry.TABLE_NAME, null, contentValues);
        contentValues.clear();

        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_PRODUCT_NAME, "The Fellowship of the Ring by J. R. R. Tolkien");
        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_PRICE, 321);
        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_QUANTITY, 15);
        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_SUPPLIER_NAME, "English Literature Publisher");
        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE_NUMBER, "569-956-21-00");
        sqLiteDatabase.insert(BooksInventoryContract.BooksEntry.TABLE_NAME, null, contentValues);
        contentValues.clear();

        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_PRODUCT_NAME, "Great Expectations by Charles Dickens");
        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_PRICE, 59);
        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_QUANTITY, 28);
        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_SUPPLIER_NAME, "Books From All Over The World");
        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE_NUMBER, "123-519-56-51");
        sqLiteDatabase.insert(BooksInventoryContract.BooksEntry.TABLE_NAME, null, contentValues);
        contentValues.clear();

        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_PRODUCT_NAME, "The Call Of The Wild");
        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_PRICE, 123);
        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_QUANTITY, 10);
        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_SUPPLIER_NAME, "The Macmillan Company");
        contentValues.put(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE_NUMBER, "569-250-65-52");
        sqLiteDatabase.insert(BooksInventoryContract.BooksEntry.TABLE_NAME, null, contentValues);
    }
}