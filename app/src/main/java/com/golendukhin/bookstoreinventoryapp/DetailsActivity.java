package com.golendukhin.bookstoreinventoryapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentStateBook.setName(editable.toString());
                invalidateOptionsMenu();
            }
        });

        priceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String priceText = editable.toString();
                double price;

                if (priceText.equals("") || priceText.equals(".") || priceText.equals("0")) {
                    price = 0.0;//Double.valueOf(editable.toString());
                } else {
                    price = Double.valueOf(editable.toString());
                }

                currentStateBook.setPrice(price);
                invalidateOptionsMenu();
            }
        });

        priceEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (currentStateBook.getPrice() == .0) {
                        priceEditText.setText("");
                    }
                }
            }
        });

        quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int quantity  = 0;
                if (!editable.toString().equals("")) {
                    quantity = Integer.valueOf(editable.toString());
                }
                currentStateBook.setQuantity(quantity);
                invalidateOptionsMenu();
            }
        });

        quantityEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (currentStateBook.getQuantity() == 0) {
                        quantityEditText.setText("");
                    }
                }
            }
        });

        supplierEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentStateBook.setSupplier(editable.toString());
                invalidateOptionsMenu();
            }
        });

        supplierPhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentStateBook.setSupplierPhone(editable.toString());
                invalidateOptionsMenu();
            }
        });
    }
}
