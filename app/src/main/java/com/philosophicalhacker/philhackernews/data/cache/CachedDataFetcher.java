package com.philosophicalhacker.philhackernews.data.cache;

import android.content.ContentResolver;
import android.database.Cursor;

import com.philosophicalhacker.philhackernews.data.DataConverter;
import com.philosophicalhacker.philhackernews.data.DataFetcher;
import com.philosophicalhacker.philhackernews.model.Item;

import java.util.List;

/**
 * Hides ugly details of content URIs and cursor-to-POJO conversion.
 *
 * Created by MattDupree on 7/18/15.
 */
public class CachedDataFetcher implements DataFetcher {

    public static final String DAGGER_INJECT_QUALIFIER = "cache";
    private ContentResolver mContentResolver;
    private DataConverter<List<Item>, Cursor> mCursorToItemConverter;

    public CachedDataFetcher(ContentResolver contentResolver, DataConverter<List<Item>, Cursor> cursorToItemConverter) {
        mContentResolver = contentResolver;
        mCursorToItemConverter = cursorToItemConverter;
    }

    @Override
    public List<Item> getTopStories(int limit) {
        Cursor cursor = mContentResolver.query(HackerNewsData.Items.CONTENT_URI, null, null, null, HackerNewsData.Items.SCORE + " DESC");
        return mCursorToItemConverter.convertData(cursor);
    }

    @Override
    public List<Item> getTopStories() {
        return getTopStories(Integer.MAX_VALUE);
    }

    @Override
    public Item getStory(int storyId) {
        return getItem(storyId);
    }

    @Override
    public Item getComment(int commentId) {
        return getItem(commentId);
    }

    @Override
    public List<Item> getCommentsForStory(Item story, int limit) {
        Cursor cursor = mContentResolver.query(HackerNewsData.Items.CONTENT_URI, null,
                HackerNewsData.Items.Selection.COMMENTS_FOR_STORY,
                HackerNewsData.Items.Selection.getCommentsForStoryArgs(story.getId()), null);
        return mCursorToItemConverter.convertData(cursor);
    }

    @Override
    public List<Item> getCommentsForStory(Item story) {
        return getCommentsForStory(story, Integer.MAX_VALUE);
    }

    private Item getItem(long itemId) {
        Cursor query = mContentResolver.query(HackerNewsData.Items.CONTENT_URI, null,
                HackerNewsData.Items.Selection.ITEM_ID,
                HackerNewsData.Items.Selection.getItemWithIdArgs(itemId), null);
        return mCursorToItemConverter.convertData(query).get(0);
    }
}
