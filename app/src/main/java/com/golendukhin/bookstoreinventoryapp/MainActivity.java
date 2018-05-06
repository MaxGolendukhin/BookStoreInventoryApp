package com.golendukhin.bookstoreinventoryapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.golendukhin.bookstoreinventoryapp.data_base.BooksInventoryContract;
import com.golendukhin.bookstoreinventoryapp.data_base.BooksInventoryDBHelper;
import com.golendukhin.bookstoreinventoryapp.data_base.DataBaseUtils;
import com.golendukhin.bookstoreinventoryapp.data_base.BooksInventoryContract.BooksEntry;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayDatabaseContent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                DataBaseUtils.insertData(this);
                displayDatabaseContent();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayDatabaseContent() {
        BooksInventoryDBHelper booksInventoryDBHelper = new BooksInventoryDBHelper(this);
        SQLiteDatabase sqLiteDatabase = booksInventoryDBHelper.getReadableDatabase();
        
        String table = BooksEntry.TABLE_NAME;
        Cursor cursor = sqLiteDatabase.query(table, null, null, null, null, null, null);

        TextView textView = findViewById(R.id.text_view);

        try {
            textView.setText("Books inventory contains " + cursor.getCount() + " items.\n\n");
            textView.append(BooksEntry._ID + " - " +
                    BooksEntry.COLUMN_BOOKS_PRODUCT_NAME + " - " +
                    BooksEntry.COLUMN_BOOKS_PRICE + " - " +
                    BooksEntry.COLUMN_BOOKS_QUANTITY + " - " +
                    BooksEntry.COLUMN_BOOKS_SUPPLIER_NAME + " - " +
                    BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE_NUMBER + "\n");

            int idColumnIndex = cursor.getColumnIndex(BooksEntry._ID);
            int productNameColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_BOOKS_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_BOOKS_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_BOOKS_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_BOOKS_SUPPLIER_NAME);
            int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE_NUMBER);

            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentProductName = cursor.getString(productNameColumnIndex);
                double currentPrice = 1.0 * cursor.getInt(priceColumnIndex) / 100;
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierPhoneNumber = cursor.getString(supplierPhoneNumberColumnIndex);

                textView.append("\n" + currentID + " - " +
                        currentProductName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplierName + " - " +
                        currentSupplierPhoneNumber);
            }
        } finally {
            cursor.close();
        }
    }
}