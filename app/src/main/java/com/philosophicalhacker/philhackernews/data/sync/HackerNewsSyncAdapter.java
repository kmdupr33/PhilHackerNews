package com.philosophicalhacker.philhackernews.data.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philosophicalhacker.philhackernews.data.DataFetcher;
import com.philosophicalhacker.philhackernews.data.cache.HackerNewsData;
import com.philosophicalhacker.philhackernews.model.Item;

import java.util.List;

/**
 *
 * Created by MattDupree on 7/18/15.
 */
public class HackerNewsSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String EXTRA_KEY_TOP_STORIES_LIMIT = "EXTRA_KEY_TOP_STORIES_LIMIT";
    private static final String TAG = HackerNewsSyncAdapter.class.getSimpleName();
    private final DataFetcher mRemoteDataFetcher;
    private final DataFetcher mCachedDataFetcher;

    public HackerNewsSyncAdapter(Context context, boolean autoInitialize,
                                 DataFetcher remoteDataFetcher,
                                 DataFetcher cachedDataFetcher) {
        super(context, autoInitialize);
        mRemoteDataFetcher = remoteDataFetcher;
        mCachedDataFetcher = cachedDataFetcher;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "Performing data sync for account: " + account);
        int limit = extras.getInt(EXTRA_KEY_TOP_STORIES_LIMIT, Integer.MAX_VALUE);
        List<Item> topStories = mRemoteDataFetcher.getTopStories(limit);
        List<Item> cachedTopStories = mCachedDataFetcher.getTopStories();
        for (int i = 0; i < topStories.size(); i++) {
            Item item = topStories.get(i);
            ContentValues contentValues = getContentValuesForStory(item);
            /*
            Normally, I'd be tempted to pull this logic into an object that's more unit testable than
            this one, but because the HackerNews api is readonly, we know that there isn't going to
            be any complicated business rules governing how we handle conflicts between the server
            and the device. I'm a little sad that there's this little piece of business logic mixed
            in with all of this android code, but time is money and a general rule of creating objects
            for tiny snippets of business logic that we know can't be more complicated might
            lead to too much overhead overall in the application anyway.
             */
            ContentResolver contentResolver = getContext().getContentResolver();
            if (cachedTopStories.contains(item)) {
                contentResolver.update(ContentUris.withAppendedId(HackerNewsData.Items.CONTENT_URI, item.getId()), contentValues, null, null);
            } else {
                contentResolver.insert(HackerNewsData.Items.CONTENT_URI, contentValues);
            }
        }
    }

    //----------------------------------------------------------------------------------
    // Helpers
    //----------------------------------------------------------------------------------
    @NonNull
    private ContentValues getContentValuesForStory(Item item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(HackerNewsData.Items._ID, item.getId());
        contentValues.put(HackerNewsData.Items.SCORE, item.getScore());
        contentValues.put(HackerNewsData.Items.TITLE, item.getTitle());
        contentValues.put(HackerNewsData.Items.AUTHOR, item.getAuthor());
        contentValues.put(HackerNewsData.Items.URL, item.getUrl());
        contentValues.put(HackerNewsData.Items.TEXT, item.getText());
        StringBuilder stringBuilder = new StringBuilder();
        int[] comments = item.getComments();
        if (comments != null) {
            for (int i=0, end=comments.length; i<end;i++) {
                stringBuilder.append(comments[i] + ",");
            }
        }
        contentValues.put(HackerNewsData.Items.COMMENTS, stringBuilder.toString());
        return contentValues;
    }
}
