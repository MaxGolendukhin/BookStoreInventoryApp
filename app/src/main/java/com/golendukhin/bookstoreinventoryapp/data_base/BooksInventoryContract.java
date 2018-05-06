package com.golendukhin.bookstoreinventoryapp.data_base;

import android.provider.BaseColumns;

import com.golendukhin.bookstoreinventoryapp.R;

public final class BooksInventoryContract {

    /**
     * Empty constructor to avoid class initialization
     */
    BooksInventoryContract() {
        throw new AssertionError(R.string.util_exception_instantiation);
    }

    public final static class BooksEntry implements BaseColumns {
        public final static String TABLE_NAME = "books";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_BOOKS_PRODUCT_NAME = "productName";
        public static final String COLUMN_BOOKS_PRICE = "price";
        public static final String COLUMN_BOOKS_QUANTITY = "quantity";
        public static final String COLUMN_BOOKS_SUPPLIER_NAME = "supplierName";
        public static final String COLUMN_BOOKS_SUPPLIER_PHONE_NUMBER = "supplierPhoneNumber";
    }
}