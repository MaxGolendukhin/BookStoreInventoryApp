package com.golendukhin.bookstoreinventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.golendukhin.bookstoreinventoryapp.data_base.BooksInventoryContract.BooksEntry;
import com.golendukhin.bookstoreinventoryapp.data_base.DataBaseUtils;

import static com.golendukhin.bookstoreinventoryapp.data_base.BooksInventoryContract.BooksEntry.CONTENT_URI;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String[] PROJECTION = new String[] {BooksEntry._ID,
            BooksEntry.COLUMN_BOOKS_PRODUCT_NAME,
            BooksEntry.COLUMN_BOOKS_PRICE, BooksEntry.COLUMN_BOOKS_QUANTITY};
    private static final int BOOKS_LOADER = 0;
    private BooksInventoryCursorAdapter booksInventoryCursorAdapter;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_activity);

       cursor = getContentResolver().query(CONTENT_URI, null, null, null, null);
        booksInventoryCursorAdapter = new BooksInventoryCursorAdapter(this, cursor, 0);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(booksInventoryCursorAdapter);

//        Button button = listView.findViewById(R.id.reduce_quantity_list_view_button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, DetailsActivity.class);
                Uri uri = ContentUris.withAppendedId(CONTENT_URI, id);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, DetailsActivity.class);
                startActivity(intent);
            }
        });



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
//                    BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE + "\n");
//
//            int idColumnIndex = cursor.getColumnIndex(BooksEntry._ID);
//            int productNameColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_BOOKS_PRODUCT_NAME);
//            int priceColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_BOOKS_PRICE);
//            int quantityColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_BOOKS_QUANTITY);
//            int supplierNameColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_BOOKS_SUPPLIER_NAME);
//            int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE);
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

    public void buttonClicked(View view) {
        Button button = (Button)view;
        int position = (int)button.getTag();
        int id = position + 1;
        Uri uri = ContentUris.withAppendedId(CONTENT_URI, id);

        cursor.move(position);




//        cursor.moveToPosition(position);
//        String bookTitle = cursor.getString(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_BOOKS_PRODUCT_NAME));
//        String price = "price: $" + String.valueOf(1.0 * cursor.getInt(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_BOOKS_PRICE)) / 100);
//        String quantity = "quantity: " + String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_BOOKS_QUANTITY)));
//        int a = 0;






        String bookTitle = cursor.getString(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_BOOKS_PRODUCT_NAME));
        int price = cursor.getInt(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_BOOKS_PRICE));
        int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_BOOKS_QUANTITY));
        String supplier = cursor.getString(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_BOOKS_SUPPLIER_NAME));
        String supplierPhone = cursor.getString(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE));

        if (quantity > 0) quantity--;

        ContentValues contentValues = new ContentValues();
        contentValues.put(BooksEntry.COLUMN_BOOKS_PRODUCT_NAME, bookTitle);
        contentValues.put(BooksEntry.COLUMN_BOOKS_PRICE, price);
        contentValues.put(BooksEntry.COLUMN_BOOKS_QUANTITY, quantity);
        contentValues.put(BooksEntry.COLUMN_BOOKS_SUPPLIER_NAME, supplier);
        contentValues.put(BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE, supplierPhone);

        int rowsAffected = getContentResolver().update(uri, contentValues, null, null);
        if (rowsAffected == 0) {
            // If no rows were affected, then there was an error with the update.
            Toast.makeText(this, getString(R.string.reducing_quantity_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the update was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.reducing_quantity_success),
                    Toast.LENGTH_SHORT).show();
        }

       getContentResolver().update(uri, contentValues, null, null);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {


        return new CursorLoader(this, CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {


        cursor.moveToPosition(2);
        String bookTitle = cursor.getString(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_BOOKS_PRODUCT_NAME));
        String price = "price: $" + String.valueOf(1.0 * cursor.getInt(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_BOOKS_PRICE)) / 100);
        String quantity = "quantity: " + String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_BOOKS_QUANTITY)));
        int a = 0;



        booksInventoryCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        booksInventoryCursorAdapter.swapCursor(null);
    }
}