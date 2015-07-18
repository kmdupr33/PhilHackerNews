package com.philosophicalhacker.philhackernews.data.content;

import android.content.ContentResolver;
import android.database.Cursor;

import com.philosophicalhacker.philhackernews.data.DataConverter;

import java.util.List;

import javax.inject.Inject;

/**
 * Hides ugly details of content URIs and cursor-to-POJO conversion.
 *
 * Created by MattDupree on 7/18/15.
 */
public class HackerNewsDataCache {

    private ContentResolver mContentResolver;
    private DataConverter<List<Integer>, Cursor> mCursorToStoryIdsConverter;

    @Inject
    public HackerNewsDataCache(ContentResolver contentResolver, DataConverter<List<Integer>, Cursor> cursorToStoryIdsConverter) {
        mContentResolver = contentResolver;
        mCursorToStoryIdsConverter = cursorToStoryIdsConverter;
    }

    public List<Integer> getTopStories() {
        Cursor cursor = mContentResolver.query(HackerNewsData.Stories.CONTENT_URI, null, null, null, HackerNewsData.Stories.SCORE + " DESC");
        return mCursorToStoryIdsConverter.convertData(cursor);
    }
}
