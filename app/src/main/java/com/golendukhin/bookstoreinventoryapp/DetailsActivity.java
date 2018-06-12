package com.golendukhin.bookstoreinventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    /**
     * Inflates details_activity
     * Gets uri from intent
     * Sets activity title whether book is being created or edited
     * Initializes all views is activity
     * Sets listeners to editTexts and buttons
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        Intent intent = getIntent();
        bookUri = intent.getData();
        if (bookUri == null) {
            setTitle(R.string.add_book_title);
            initialStateBook = new Book();
            currentStateBook = new Book();
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

        addPriceButton = findViewById(R.id.add_price_button);
        reducePriceButton = findViewById(R.id.reduce_price_button);
        addQuantityButton = findViewById(R.id.add_quantity_button);
        reduceQuantityButton = findViewById(R.id.reduce_quantity_button);

        setButtonsListeners();
    }

    /**
     * Predefined by interface method
     * Creates new loader
     */
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, bookUri, null, null, null, null);
    }

    /**
     * Predefined by interface method
     * Updates state of activity views with new values
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            initialStateBook = new Book(cursor);
            currentStateBook = new Book(initialStateBook);

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

    /**
     * Inflate the menu options from the res/menu/details_menu.xml file.
     * This adds menu items to the app bar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    /**
     * Before menu inflating checks mode of details activity
     * If details activity is used to create new object, then delete options not required
     * If state of initialStateBook is different to currentStateBook then
     * 'Save' button is added to menu
     */
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

    /**
     * Responding to menu button clicks
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                if (saveBook()) {
                    finish();
                }
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the book hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (initialStateBook.isEqual(currentStateBook)) {
                    NavUtils.navigateUpFromSameTask(DetailsActivity.this);
                    return true;
                }
                //Otherwise if there are unsaved changes, setup a dialog to warn the user.
                //Create a click listener to handle the user confirming that
                //changes should be discarded.
                showUnsavedChangesDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Helper method
     * Saves book via content resolver if "Save" menu item was pressed
     */
    private boolean saveBook() {
        String bookName = currentStateBook.getName();
        int price = (int) currentStateBook.getPrice() * 100;
        int quantity = currentStateBook.getQuantity();
        String supplier = currentStateBook.getSupplier();
        String supplierPhone = currentStateBook.getSupplierPhone();

        if (currentStateBook.hasEmptyFields()) {
            if (TextUtils.isEmpty(bookName)) {
                nameEditText.setError(getString(R.string.book_needs_name_prompt));
            }

            if (price == 0) {
                priceEditText.setError(getString(R.string.book_needs_price_prompt));
            }

            if (quantity == 0) {
                quantityEditText.setError(getString(R.string.book_needs_quantity_prompt));
            }

            if (TextUtils.isEmpty(supplier)) {
                supplierEditText.setError(getString(R.string.book_needs_supplier_prompt));
            }

            if (TextUtils.isEmpty(supplierPhone)) {
                supplierPhoneEditText.setError(getString(R.string.book_needs_supplier_phone_number_prompt));
            }
            return false;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(BooksEntry.COLUMN_BOOKS_PRODUCT_NAME, bookName);
        contentValues.put(BooksEntry.COLUMN_BOOKS_PRICE, price);
        contentValues.put(BooksEntry.COLUMN_BOOKS_QUANTITY, quantity);
        contentValues.put(BooksEntry.COLUMN_BOOKS_SUPPLIER_NAME, supplier);
        contentValues.put(BooksEntry.COLUMN_BOOKS_SUPPLIER_PHONE, supplierPhone);

        if (bookUri == null) {
            Uri uri = getContentResolver().insert(BooksEntry.CONTENT_URI, contentValues);
            Toast.makeText(this, uri == null ?
                    getString(R.string.insert_book_failed) :
                    getString(R.string.insert_book_successful), Toast.LENGTH_SHORT).show();
        } else {
            int rowsAffected = getContentResolver().update(bookUri, contentValues, null, null);
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.update_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.update_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    /**
     * Arises if user clicks "Delete" menu item to avoid unnecessary deletion by mistake
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the book in the database.
     */
    private void deleteBook() {
        // Only perform the delete if this is an existing book.
        if (bookUri != null) {
            // Call the ContentResolver to delete the book at the given content URI.
            // Pass in null for the selection and selection args because the bookUri
            // content URI already identifies the book that we want.
            int rowsDeleted = getContentResolver().delete(bookUri, null, null);
            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.delete_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.delete_book_successful),
                        Toast.LENGTH_SHORT).show();
                // Close the activity
                finish();
            }
        }
    }

    /**
     * Arises if user clicks "Back" menu item and book state is not saved to warn not to loose unsaved data
     */
    private void showUnsavedChangesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NavUtils.navigateUpFromSameTask(DetailsActivity.this);
            }
        });
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Sets edit text listeners
     */
    private void setEditTextListeners() {
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            /**
             * If title of the book is changed then menu is to be updated
             */
            @Override
            public void afterTextChanged(Editable editable) {
                currentStateBook.setName(editable.toString().trim());
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

            /**
             * Validates sum input by user
             * If it is not correct sum is set to null
             * Also updates state of currentStateBook and updates menu if needed
             */
            @Override
            public void afterTextChanged(Editable editable) {
                String priceText = priceEditText.getText().toString();
                double price;
                try {
                    price = Double.parseDouble(priceText);
                } catch (Exception e) {
                    price = 0.0;
                }
                price = Math.round(price * 100.0) / 100.0;

                currentStateBook.setPrice(price);
                invalidateOptionsMenu();
            }
        });

        priceEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            /**
             * Triggered when edit text looses focus
             * To be sure price will be displayed properly despite what user has input
             * If there are more then two decimal places, user will be warned that price will be rounded up to two places
             * If price is zero, then hint will be displayed
             * If user ha input any incorrect, unparsable value, it will be set to  zero
             */
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String priceText = priceEditText.getText().toString();
                    double price;

                    try {
                        price = Double.parseDouble(priceText);
                    } catch (Exception e) {
                        price = 0.0;
                    }

                    if (price == 0.0) {
                        priceEditText.setText("");
                    } else {
                        String[] splitted = priceText.split("\\.");
                        if (splitted.length > 1 && splitted[1].length() > 2) {
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.more_than_two_decimal_places_in_price_toast),
                                    Toast.LENGTH_LONG).show();
                        }
                        price = Math.round(price * 100.0) / 100.0;
                        priceEditText.setText(String.valueOf(price));
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

            /**
             * Validates menu if edit text is updated
             */
            @Override
            public void afterTextChanged(Editable editable) {
                int quantity = 0;
                //if user leaves field blank, it is impossible to convert to integer
                if (!editable.toString().equals("")) {
                    quantity = Integer.valueOf(editable.toString());
                }
                currentStateBook.setQuantity(quantity);
                invalidateOptionsMenu();
            }
        });

        quantityEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            /**
             * Triggered after edit text has lost its focus
             * Validates input value and if it is zero, then need to empty value
             */
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    //if quantity is zero need to set hint to edit text
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

            /**
             * If supplier name is changed then menu is to be updated
             */
            @Override
            public void afterTextChanged(Editable editable) {
                currentStateBook.setSupplier(editable.toString().trim());
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

            /**
             * If supplier phone number is changed then menu is to be updated
             */
            @Override
            public void afterTextChanged(Editable editable) {
                currentStateBook.setSupplierPhone(editable.toString().trim());
                invalidateOptionsMenu();
            }
        });
    }

    /**
     * Sets listeners to buttons
     */
    private void setButtonsListeners() {
        addPriceButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Triggered when "Add price" button is clicked
             * Increases price to one $
             */
            @Override
            public void onClick(View view) {
                double price = currentStateBook.getPrice();
                price++;
                currentStateBook.setPrice(price);
                priceEditText.setText(String.valueOf(price));
            }
        });

        reducePriceButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Triggered when "Reduce price" button is clicked
             * Decreases price with one $
             */
            @Override
            public void onClick(View view) {
                double price = currentStateBook.getPrice();
                if (price >= 1) {
                    price--;
                }
                currentStateBook.setPrice(price);
                if (price == .0) {
                    priceEditText.setText("");
                } else {
                    priceEditText.setText(String.valueOf(price));
                }
            }
        });

        addQuantityButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Triggered when "Add quantity" button is pressed
             * Increases quantity with one item
             */
            @Override
            public void onClick(View view) {
                int quantity = currentStateBook.getQuantity();
                quantity++;
                currentStateBook.setQuantity(quantity);
                quantityEditText.setText(String.valueOf(quantity));
            }
        });

        reduceQuantityButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Triggered when "Reduce quantity" button is pressed
             * Decreases quantity with one item
             */
            @Override
            public void onClick(View view) {
                int quantity = currentStateBook.getQuantity();
                if (quantity >= 1) {
                    quantity--;
                }
                if (quantity == 0) {
                    quantityEditText.setText("");
                } else {
                    quantityEditText.setText(String.valueOf(quantity));
                }
            }
        });
    }

    /**
     * XML predefined method
     * Called if "Call" button is pressed via details activity
     */
    public void callSupplier(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + currentStateBook.getSupplierPhone()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}