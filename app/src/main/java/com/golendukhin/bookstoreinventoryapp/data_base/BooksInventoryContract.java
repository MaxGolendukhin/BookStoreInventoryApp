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

        /** The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS_INVENTORY);

        public final static String TABLE_NAME = "books";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_BOOKS_PRODUCT_NAME = "productName";
        public static final String COLUMN_BOOKS_PRICE = "price";
        public static final String COLUMN_BOOKS_QUANTITY = "quantity";
        public static final String COLUMN_BOOKS_SUPPLIER_NAME = "supplierName";
        public static final String COLUMN_BOOKS_SUPPLIER_PHONE = "supplierPhoneNumber";
    }
}