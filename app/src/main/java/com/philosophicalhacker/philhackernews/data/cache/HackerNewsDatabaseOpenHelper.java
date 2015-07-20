package com.philosophicalhacker.philhackernews.data.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MattDupree on 7/18/15.
 */
public class HackerNewsDatabaseOpenHelper extends SQLiteOpenHelper {

    public HackerNewsDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + HackerNewsData.Items.TABLE_NAME
                + "(" + HackerNewsData.Items._ID + " INTEGER PRIMARY KEY, "
                + HackerNewsData.Items.SCORE + " INTEGER, "
                + HackerNewsData.Items.TITLE + " TEXT, "
                + HackerNewsData.Items.AUTHOR + " TEXT, "
                + HackerNewsData.Items.URL + " TEXT, "
                + HackerNewsData.Items.TEXT + " TEXT, "
                + HackerNewsData.Items.COMMENTS + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
