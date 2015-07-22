package com.philosophicalhacker.philhackernews.data.cache;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.philosophicalhacker.philhackernews.model.Item;

/**
 *
 * Created by MattDupree on 7/21/15.
 */
public class HackerNewsCache {

    private ContentResolver mContentResolver;

    public HackerNewsCache(ContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }
    //----------------------------------------------------------------------------------
    // Public Methods
    //----------------------------------------------------------------------------------
    public void updateItem(Item item) {
        ContentValues contentValuesForItem = getContentValuesForItem(item);
        mContentResolver.update(ContentUris.withAppendedId(HackerNewsData.Items.CONTENT_URI, item.getId()),
                contentValuesForItem,
                null, null);
    }

    public void insertItem(Item item) {
        ContentValues contentValues = getContentValuesForItem(item);
        mContentResolver.insert(HackerNewsData.Items.CONTENT_URI, contentValues);
    }

    //----------------------------------------------------------------------------------
    // Helpers
    //----------------------------------------------------------------------------------
    @NonNull
    private ContentValues getContentValuesForItem(Item item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(HackerNewsData.Items._ID, item.getId());
        contentValues.put(HackerNewsData.Items.TYPE, item.getType());
        contentValues.put(HackerNewsData.Items.SCORE, item.getScore());
        contentValues.put(HackerNewsData.Items.TITLE, item.getTitle());
        contentValues.put(HackerNewsData.Items.AUTHOR, item.getAuthor());
        contentValues.put(HackerNewsData.Items.URL, item.getUrl());
        contentValues.put(HackerNewsData.Items.TEXT, item.getText());
        contentValues.put(HackerNewsData.Items.PARENT, item.getParent());
        contentValues.put(HackerNewsData.Items.DELETED, item.isDeleted());
        StringBuilder stringBuilder = new StringBuilder();
        int[] comments = item.getComments();
        if (comments != null) {
            for (int i=0, end=comments.length; i<end;i++) {
                stringBuilder.append(comments[i]).append(",");
            }
        }
        contentValues.put(HackerNewsData.Items.COMMENTS, stringBuilder.toString());
        return contentValues;
    }

    public void notifyCacheUpdated() {
        mContentResolver.notifyChange(HackerNewsData.Items.CONTENT_URI, null);
    }
}
