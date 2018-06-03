package com.golendukhin.bookstoreinventoryapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.golendukhin.bookstoreinventoryapp.data_base.BooksInventoryContract.BooksEntry;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int BOOKS_LOADER = 0;

    private Uri bookUri;

    private EditText nameEditText;
    private EditText priceEditText;
    private EditText quantityEditText;
    private EditText supplierEditText;
    private EditText supplierPhoneEditText;

    private Button reduceQuantityButton;
    private Button addQuantityButton;
    private Button reducePriceButton;
    private Button addPriceButton;

    private Book initialStateBook;
    private Book currentStateBook;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        //invalidateOptionsMenu();



        Intent intent = getIntent();
        bookUri = intent.getData();
        if (bookUri == null) {
            setTitle(R.string.add_book_title);
            initialStateBook = new Book();
            currentStateBook = new Book();
            //invalidateOptionsMenu();
        } else {
            setTitle(R.string.details_book_title);
            getLoaderManager().initLoader(BOOKS_LOADER, null, this);
        }


        nameEditText = findViewById(R.id.name_edit_text);
        priceEditText = findViewById(R.id.price_edit_text);
        quantityEditText = findViewById(R.id.quantity_edit_text);
        supplierEditText = findViewById(R.id.supplier_edit_text);
        supplierPhoneEditText = findViewById(R.id.supplier_phone_number_edit_text);

        setEditTextListeners();

        reduceQuantityButton = findViewById(R.id.reduce_quantity_button);
        addQuantityButton = findViewById(R.id.add_quantity_button);
        reducePriceButton = findViewById(R.id.reduce_price_button);
        addPriceButton = findViewById(R.id.add_price_button);


//todo add if needed
//        mNameEditText.setOnTouchListener(mTouchListener);
//        mBreedEditText.setOnTouchListener(mTouchListener);
//        mWeightEditText.setOnTouchListener(mTouchListener);
//        mGenderSpinner.setOnTouchListener(mTouchListener);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, bookUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            currentStateBook = new Book(cursor);

            nameEditText.setText(currentStateBook.getName());
            priceEditText.setText(String.valueOf(currentStateBook.getPrice()));
            quantityEditText.setText(String.valueOf(currentStateBook.getQuantity()));
            supplierEditText.setText(currentStateBook.getSupplier());
            supplierPhoneEditText.setText(currentStateBook.getSupplierPhone());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (bookUri == null) {
            MenuItem actionDeleteMenuItem = menu.findItem(R.id.action_delete);
            actionDeleteMenuItem.setVisible(false);
        }

        MenuItem actionSaveMenuItem = menu.findItem(R.id.action_save);
        actionSaveMenuItem.setVisible(!initialStateBook.isEqual(currentStateBook));

        return true;
    }

    private void setEditTextListeners() {

    }


}
