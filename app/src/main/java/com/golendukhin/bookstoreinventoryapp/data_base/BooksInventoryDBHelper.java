package com.golendukhin.bookstoreinventoryapp.data_base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.golendukhin.bookstoreinventoryapp.data_base.BooksInventoryContract.BooksEntry;

public class BooksInventoryDBHelper extends SQLiteOpenHelper {
    /**
     * Database version. If database will be updated then value is to be incremented
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Database name. Used to initialize data base helper
     */
    private static final String DATABASE_NAME = "booksInventory.db";

    /**
     * Used for deleting whole database
     */
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE " + BooksInventoryContract.BooksEntry.TABLE_NAME + ";";

    BooksInventoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Predefined method
     * Used to create database app will interact with
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + BooksEntry.TABLE_NAME + "(" +
                BooksEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BooksEntry.COLUMN_BOOKS_PRODUCT_NAME + " TEXT NOT NULL, " +
                BooksEntry.COLUMN_BOOKS_PRICE + " INTEGER DEFAULT 0, " +
                BooksEntry.COLUMN_BOOKS_QUANTITY + " INTEGER DEFAULT 0, " +
                BooksEntry.COLUMN_BOOKS_SUPPLIER_NAME + " TEXT NOT NULL, " +
                BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    /**
     * Predefined method
     * Used to upgrade database
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}