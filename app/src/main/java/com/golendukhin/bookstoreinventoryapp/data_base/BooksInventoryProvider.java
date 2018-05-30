package com.golendukhin.bookstoreinventoryapp.data_base;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.golendukhin.bookstoreinventoryapp.data_base.BooksInventoryContract.BooksEntry;

//import static com.golendukhin.bookstoreinventoryapp.data_base.BooksInventoryContract.BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE_NUMBER;

public class BooksInventoryProvider extends ContentProvider {

    private BooksInventoryDBHelper booksInventoryDBHelper;

    /** Tag for the log messages */
    public static final String LOG_TAG = BooksInventoryProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the books inventory table */
    private static final int BOOKS = 100;

    /** URI matcher code for the content URI for a single item in the books inventory table */
    private static final int BOOK_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        sUriMatcher.addURI(BooksInventoryContract.CONTENT_AUTHORITY,
                BooksInventoryContract.PATH_BOOKS_INVENTORY, BOOKS);
        sUriMatcher.addURI(BooksInventoryContract.CONTENT_AUTHORITY,
                BooksInventoryContract.PATH_BOOKS_INVENTORY + "/#", BOOK_ID);
    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        booksInventoryDBHelper = new BooksInventoryDBHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase sqLiteDatabase = booksInventoryDBHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                cursor = sqLiteDatabase.query(BooksInventoryContract.BooksEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ID:
                selection = BooksInventoryContract.BooksEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = sqLiteDatabase.query(BooksInventoryContract.BooksEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
            final int match = sUriMatcher.match(uri);
            switch (match) {
                case BOOKS:
                    return BooksInventoryContract.BooksEntry.CONTENT_LIST_TYPE;
                case BOOK_ID:
                    return BooksInventoryContract.BooksEntry.CONTENT_ITEM_TYPE;
                default:
                    throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
            }
    }

    /**
     * Insert a bok item into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        // Check that the name is not null
        String name = contentValues.getAsString(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_PRODUCT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Book requires a name");
        }

        Integer price = contentValues.getAsInteger(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_PRICE);
        if (price != null && price < 0 ) {
            throw new IllegalArgumentException("Book price has to be positive number");
        }

        Integer quantity = contentValues.getAsInteger(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_QUANTITY);
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException("Books item quantity has to be more then zero");
        }

        String supplierName =
                contentValues.getAsString(BooksEntry.COLUMN_BOOKS_SUPPLIER_NAME);
        if (supplierName == null) {
            throw new IllegalArgumentException("Supplier requires a name");
        }

        String supplierPhoneNumber = contentValues.getAsString(BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE_NUMBER);
        if (supplierPhoneNumber == null) {
            throw new IllegalArgumentException("Supplier has to have a phone to contact");
        }

        // Get writable database
        SQLiteDatabase database = booksInventoryDBHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(BooksInventoryContract.BooksEntry.TABLE_NAME, null, contentValues);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writable database
        SQLiteDatabase sqLiteDatabase = booksInventoryDBHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                rowsDeleted = sqLiteDatabase.delete(BooksInventoryContract.BooksEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case BOOK_ID:
                // Delete a single row given by the ID in the URI
                selection = BooksInventoryContract.BooksEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = sqLiteDatabase.delete(BooksInventoryContract.BooksEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);

        }
        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return updateBook(uri, contentValues, selection, selectionArgs);
            case BOOK_ID:
                selection = BooksEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateBook(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if (contentValues.containsKey(BooksEntry.COLUMN_BOOKS_PRODUCT_NAME)) {
            String name = contentValues.getAsString(BooksEntry.COLUMN_BOOKS_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Book requires a name");
            }
        }

        if (contentValues.containsKey(BooksEntry.COLUMN_BOOKS_PRICE)) {
            Integer price = contentValues.getAsInteger(BooksEntry.COLUMN_BOOKS_PRICE);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("Book price has to be positive number");
            }
        }

        if (contentValues.containsKey(BooksEntry.COLUMN_BOOKS_QUANTITY)) {
            Integer quantity = contentValues.getAsInteger(BooksEntry.COLUMN_BOOKS_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("Books item quantity has to be more then zero");
            }
        }

        if (contentValues.containsKey(BooksEntry.COLUMN_BOOKS_SUPPLIER_NAME)) {
            String supplierName = contentValues.getAsString(BooksEntry.COLUMN_BOOKS_SUPPLIER_NAME);
            if (supplierName == null) {
                throw new IllegalArgumentException("Supplier requires a name");
            }
        }

        if (contentValues.containsKey(BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE_NUMBER)) {
            String supplierPhoneNumber = contentValues.getAsString(BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE_NUMBER);
            if (supplierPhoneNumber == null) {
                throw new IllegalArgumentException("Supplier has to have a phone to contact");
            }
        }

        SQLiteDatabase sqLiteDatabase = booksInventoryDBHelper.getWritableDatabase();
        int rowsUpdated = sqLiteDatabase.update(BooksEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}