package com.golendukhin.bookstoreinventoryapp;

import android.database.Cursor;

import com.golendukhin.bookstoreinventoryapp.data_base.BooksInventoryContract;

class Book {
    private String name;
    private double price;
    private int quantity;
    private String supplier;
    private String supplierPhone;

    Book () {
        name = "";
        price = 0;
        quantity = 0;
        supplier = "";
        supplierPhone = "";
    }

    Book(String name, double price, int quantity, String supplier, String supplierPhone) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.supplier = supplier;
        this.supplierPhone = supplierPhone;
    }

    Book(Cursor cursor) {
        this.name = cursor.getString(cursor.getColumnIndex(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_PRODUCT_NAME));
        this.price = 1.0 * cursor.getInt(cursor.getColumnIndexOrThrow(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_PRICE)) / 100;
        this.quantity = cursor.getInt(cursor.getColumnIndexOrThrow(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_PRICE));
        this.supplier = cursor.getString(cursor.getColumnIndex(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_SUPPLIER_NAME));
        this.supplierPhone = cursor.getString(cursor.getColumnIndex(BooksInventoryContract.BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE));
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }

    boolean isEqual(Book book) {
        return book.getName().equals(this.name) &&
                book.getPrice() == this.price &&
                book.getQuantity() == this.quantity &&
                book.getSupplier().equals(this.supplier) &&
                book.getSupplierPhone().equals(this.supplierPhone);
    }
}