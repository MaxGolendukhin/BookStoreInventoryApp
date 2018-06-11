package com.golendukhin.bookstoreinventoryapp.data_base;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.golendukhin.bookstoreinventoryapp.R;

public final class BooksInventoryContract {

    public static final String CONTENT_AUTHORITY = "com.golendukhin.bookstoreinventoryapp";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BOOKS_INVENTORY = "booksInventory";

    /**
     * Empty constructor to avoid class initialization
     */
    BooksInventoryContract() {
        throw new AssertionError(R.string.util_exception_instantiation);
    }

    public final static class BooksEntry implements BaseColumns {
        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of books.
         */
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS_INVENTORY;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single book.
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS_INVENTORY;

        /**
         * The content URI to access the pet data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS_INVENTORY);

        /**
         * Name of the data base where all information is stores
         */
        public final static String TABLE_NAME = "books";

        /**
         * Name of the key column in {@link #TABLE_NAME} database
         */
        public static final String _ID = BaseColumns._ID;

        /**
         * Name of the column with title of the book in {@link #TABLE_NAME} database
         */
        public static final String COLUMN_BOOKS_PRODUCT_NAME = "productName";

        /**
         * Name of the column with price of the book in {@link #TABLE_NAME} database
         */
        public static final String COLUMN_BOOKS_PRICE = "price";

        /**
         * Name of the column with quantity of the book in {@link #TABLE_NAME} database
         */
        public static final String COLUMN_BOOKS_QUANTITY = "quantity";

        /**
         * Name of the column with name of supplier of the book in {@link #TABLE_NAME} database
         */
        public static final String COLUMN_BOOKS_SUPPLIER_NAME = "supplierName";

        /**
         * Name of the column with supplier phone number of the book in {@link #TABLE_NAME} database
         */
        public static final String COLUMN_BOOKS_SUPPLIER_PHONE = "supplierPhoneNumber";
    }
}