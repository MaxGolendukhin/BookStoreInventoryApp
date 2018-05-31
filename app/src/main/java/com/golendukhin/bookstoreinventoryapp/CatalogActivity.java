package com.golendukhin.bookstoreinventoryapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.golendukhin.bookstoreinventoryapp.data_base.BooksInventoryContract.BooksEntry;
import com.golendukhin.bookstoreinventoryapp.data_base.BooksInventoryDBHelper;
import com.golendukhin.bookstoreinventoryapp.data_base.DataBaseUtils;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String[] PROJECTION = new String[] {BooksEntry._ID,
            BooksEntry.COLUMN_BOOKS_PRODUCT_NAME,
            BooksEntry.COLUMN_BOOKS_PRICE, BooksEntry.COLUMN_BOOKS_QUANTITY};
    private static final int BOOKS_LOADER = 0;
    private BooksInventoryCursorAdapter booksInventoryCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        Cursor cursor = getContentResolver().query(BooksEntry.CONTENT_URI, PROJECTION, null, null, null);


        while (cursor.moveToNext()) {
            String bookTitle = cursor.getString(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_BOOKS_PRODUCT_NAME));
            int a = 0;
        }


        booksInventoryCursorAdapter = new BooksInventoryCursorAdapter(this, cursor, 0);

        ListView listView = findViewById(R.id.list);
        listView.setAdapter(booksInventoryCursorAdapter);

        getLoaderManager().initLoader(BOOKS_LOADER, null, this);
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
                //displayDatabaseContent();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    /**
//     * Displays content of database
//     */
//    private void displayDatabaseContent() {
//        BooksInventoryDBHelper booksInventoryDBHelper = new BooksInventoryDBHelper(this);
//        SQLiteDatabase sqLiteDatabase = booksInventoryDBHelper.getReadableDatabase();
//
//        String table = BooksEntry.TABLE_NAME;
//        Cursor cursor = sqLiteDatabase.query(table, null, null, null, null, null, null);
//
//        TextView textView = findViewById(R.id.text_view);
//
//        try {
//            textView.setText("Books inventory contains " + cursor.getCount() + " items.\n\n");
//            textView.append(BooksEntry._ID + " - " +
//                    BooksEntry.COLUMN_BOOKS_PRODUCT_NAME + " - " +
//                    BooksEntry.COLUMN_BOOKS_PRICE + " - " +
//                    BooksEntry.COLUMN_BOOKS_QUANTITY + " - " +
//                    BooksEntry.COLUMN_BOOKS_SUPPLIER_NAME + " - " +
//                    BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE_NUMBER + "\n");
//
//            int idColumnIndex = cursor.getColumnIndex(BooksEntry._ID);
//            int productNameColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_BOOKS_PRODUCT_NAME);
//            int priceColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_BOOKS_PRICE);
//            int quantityColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_BOOKS_QUANTITY);
//            int supplierNameColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_BOOKS_SUPPLIER_NAME);
//            int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE_NUMBER);
//
//            while (cursor.moveToNext()) {
//                int currentID = cursor.getInt(idColumnIndex);
//                String currentProductName = cursor.getString(productNameColumnIndex);
//                double currentPrice = 1.0 * cursor.getInt(priceColumnIndex) / 100;
//                int currentQuantity = cursor.getInt(quantityColumnIndex);
//                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
//                String currentSupplierPhoneNumber = cursor.getString(supplierPhoneNumberColumnIndex);
//
//                textView.append("\n" + currentID + " - " +
//                        currentProductName + " - " +
//                        currentPrice + " - " +
//                        currentQuantity + " - " +
//                        currentSupplierName + " - " +
//                        currentSupplierPhoneNumber);
//            }
//        } finally {
//            cursor.close();
//        }
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, BooksEntry.CONTENT_URI, PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        booksInventoryCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        booksInventoryCursorAdapter.swapCursor(null);

    }
}