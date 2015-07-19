package com.philosophicalhacker.philhackernews.data.cache;

import android.content.ContentResolver;
import android.database.Cursor;

import com.philosophicalhacker.philhackernews.data.DataConverter;
import com.philosophicalhacker.philhackernews.data.DataFetcher;
import com.philosophicalhacker.philhackernews.model.Story;

import java.util.List;

/**
 * Hides ugly details of content URIs and cursor-to-POJO conversion.
 *
 * Created by MattDupree on 7/18/15.
 */
public class CachedDataFetcher implements DataFetcher {

    public static final String DAGGER_INJECT_QUALIFIER = "cache";
    private ContentResolver mContentResolver;
    private DataConverter<List<Story>, Cursor> mCursorToStoryIdsConverter;

    public CachedDataFetcher(ContentResolver contentResolver, DataConverter<List<Story>, Cursor> cursorToStoryIdsConverter) {
        mContentResolver = contentResolver;
        mCursorToStoryIdsConverter = cursorToStoryIdsConverter;
    }

    @Override
    public List<Story> getTopStories(int limit) {
        Cursor cursor = mContentResolver.query(HackerNewsData.Stories.CONTENT_URI, null, null, null, HackerNewsData.Stories.SCORE + " DESC");
        return mCursorToStoryIdsConverter.convertData(cursor);
    }

    @Override
    public List<Story> getTopStories() {
        return getTopStories(Integer.MAX_VALUE);
    }

    @Override
    public Story getStory(int storyId) {
        return null;
    }
}
