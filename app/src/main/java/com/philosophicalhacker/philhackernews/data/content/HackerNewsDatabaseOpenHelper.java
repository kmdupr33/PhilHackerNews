package com.philosophicalhacker.philhackernews.data.content;

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
        db.execSQL("CREATE TABLE " + HackerNewsData.Stories.TABLE_NAME
                + "(" + HackerNewsData.Stories._ID + " INTEGER PRIMARY KEY, "
                + HackerNewsData.Stories.SCORE + " INTEGER, "
                + HackerNewsData.Stories.TITLE + " TEXT, "
                + HackerNewsData.Stories.AUTHOR + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
