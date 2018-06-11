package com.golendukhin.bookstoreinventoryapp;

import android.database.Cursor;

import com.golendukhin.bookstoreinventoryapp.data_base.BooksInventoryContract;

class Book {
    private String name;
    private double price;
    private int quantity;
    private String supplier;
    private String supplierPhone;

    /**
     * Base constructor
     * Used if need to initialize empty Book object
     */
    Book() {
        name = "";
        price = 0;
        quantity = 0;
        supplier = "";
        supplierPhone = "";
    }

    /**
     * Constructor to initialize Book object based on another obe
     * @param book earlier defined object
     */
    Book(Book book) {
        this.name = book.getName();
        this.price = book.getPrice();
        this.quantity = book.getQuantity();
        this.supplier = book.getSupplier();
        this.supplierPhone = book.supplierPhone;
    }

    /**
     * Used if need to define Book object from cursor
     *
     * @param cursor from which object is initialized
     */
    Book(Cursor cursor) {
        this.name = cursor.getString(cursor.getColumnIndex(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_PRODUCT_NAME));
        this.price = 1.0 * cursor.getInt(cursor.getColumnIndexOrThrow(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_PRICE)) / 100;
        this.quantity = cursor.getInt(cursor.getColumnIndexOrThrow(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_QUANTITY));
        this.supplier = cursor.getString(cursor.getColumnIndex(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_SUPPLIER_NAME));
        this.supplierPhone = cursor.getString(cursor.getColumnIndex(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }

    /**
     * Compares two Book objects
     * Main method for which class was implemented
     * While editing book in detail activity current Book is constantly compared to initial
     * If they are not equal menu button 'save' is displayed in appbar
     *
     * @param book initial object to compare
     * @return true if objects are equal, false otherwise
     */
    boolean isEqual(Book book) {
        return book.getName().equals(this.name) &&
                book.getPrice() == this.price &&
                book.getQuantity() == this.quantity &&
                book.getSupplier().equals(this.supplier) &&
                book.getSupplierPhone().equals(this.supplierPhone);
    }
}