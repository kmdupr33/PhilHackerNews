package com.philosophicalhacker.philhackernews.data.cache;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.philosophicalhacker.philhackernews.PhilHackerNewsApplication;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class HackerNewsContentProvider extends ContentProvider {

    @Inject
    SQLiteDatabase mHackerNewsDatabase;

    @Override
    public boolean onCreate() {
        //noinspection ResourceType
        ObjectGraph objectGraph = (ObjectGraph) getContext().getSystemService(PhilHackerNewsApplication.OBJECT_GRAPH);
        objectGraph.inject(this);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = mHackerNewsDatabase.query(HackerNewsData.Items.TABLE_NAME,
                                                                      projection,
                                                                      selection,
                                                                      selectionArgs, null, null,
                                                                      sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        insertWithoutNotifyingChange(uri, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int numRowsAffected = mHackerNewsDatabase.update(HackerNewsData.Items.TABLE_NAME,
                                                                              values,
                                                                              HackerNewsData.Items._ID + " = ?",
                                                                              new String[]{uri.getLastPathSegment()});
        getContext().getContentResolver().notifyChange(uri, null);
        return numRowsAffected;
    }

    @Override
    public int bulkInsert(Uri uri, @NonNull ContentValues[] values) {
        int numValues = values.length;
        for (int i = 0; i < numValues; i++) {
            insertWithoutNotifyingChange(uri, values[i]);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return numValues;
    }

    //----------------------------------------------------------------------------------
    // Helpers
    //----------------------------------------------------------------------------------
    private Uri insertWithoutNotifyingChange(Uri uri, ContentValues contentValues) {
        mHackerNewsDatabase.insert(HackerNewsData.Items.TABLE_NAME, null, contentValues);
        return uri;
    }
}
