package com.philosophicalhacker.philhackernews.data.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.philosophicalhacker.philhackernews.PhilHackerNewsApplication;
import com.philosophicalhacker.philhackernews.data.HackerNewsDataSource;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class HackerNewsContentProvider extends ContentProvider {

    @Inject
    HackerNewsDatabaseOpenHelper mSQLiteOpenHelper;

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
        Cursor cursor = mSQLiteOpenHelper.getWritableDatabase().query(HackerNewsData.Stories.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
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
        mSQLiteOpenHelper.getWritableDatabase().insert(HackerNewsData.Stories.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return mSQLiteOpenHelper.getWritableDatabase().update(HackerNewsData.Stories.TABLE_NAME, values, "_id = ?", new String[]{uri.getLastPathSegment()});
    }
}
