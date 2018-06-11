package com.golendukhin.bookstoreinventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import static com.golendukhin.bookstoreinventoryapp.data_base.BooksInventoryContract.BooksEntry;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int BOOKS_LOADER = 0;
    private BooksInventoryCursorAdapter booksInventoryCursorAdapter;

    /**
     * Inflates catalog_activity
     * Sets title of activity
     * Initializes cursor object
     * Populates adapter with cursor
     * Initializes list view and populates it with adapter
     * Sets empty view to list view
     * Defines list view item press method
     * Defines fab item press method
     * Initializes content resolver
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_activity);

        setTitle(R.string.catalog_title);

        Cursor cursor = getContentResolver().query(BooksEntry.CONTENT_URI,
                null, null, null, null);
        booksInventoryCursorAdapter = new BooksInventoryCursorAdapter(this, cursor, 0);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(booksInventoryCursorAdapter);

        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Detects if item was clicked and moves to details activity
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, DetailsActivity.class);
                Uri uri = ContentUris.withAppendedId(BooksEntry.CONTENT_URI, id);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Detects if fab was pressed and launches empty details activity
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, DetailsActivity.class);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(BOOKS_LOADER, null, this);
    }

    /**
     * Predefined by interface method
     * Creates new loader
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, BooksEntry.CONTENT_URI,
                null, null, null, null);
    }

    /**
     * Predefined by interface method
     * Switches adapter if it has been changed
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        booksInventoryCursorAdapter.swapCursor(cursor);
    }

    /**
     * Predefined by interface method
     * Populates adapter with empty cursor
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        booksInventoryCursorAdapter.swapCursor(null);
    }
}