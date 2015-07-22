package com.philosophicalhacker.philhackernews.data.sync;

import android.accounts.Account;
import android.content.ContentResolver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philosophicalhacker.philhackernews.data.DataFetcher;
import com.philosophicalhacker.philhackernews.data.cache.CachedDataFetcher;
import com.philosophicalhacker.philhackernews.data.cache.HackerNewsCache;
import com.philosophicalhacker.philhackernews.data.cache.HackerNewsData;
import com.philosophicalhacker.philhackernews.data.remote.RemoteDataFetcher;
import com.philosophicalhacker.philhackernews.model.Item;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Makes requests to synchronize cached device data with remote api data.
 *
 * Created by MattDupree on 7/19/15.
 */
public class DataSynchronizer {

    private static final String TAG = DataSynchronizer.class.getSimpleName();

    private Account mAccount;
    private HackerNewsCache mHackerNewsCache;
    private final DataFetcher mRemoteDataFetcher;
    private final DataFetcher mCachedDataFetcher;

    @Inject
    public DataSynchronizer(Account account,
                            HackerNewsCache hackerNewsCache,
                            @Named(RemoteDataFetcher.DAGGER_INJECT_QUALIFIER) DataFetcher remoteDataFetcher,
                            @Named(CachedDataFetcher.DAGGER_INJECT_QUALIFIER) DataFetcher cachedDataFetcher) {
        mAccount = account;
        mHackerNewsCache = hackerNewsCache;
        mRemoteDataFetcher = remoteDataFetcher;
        mCachedDataFetcher = cachedDataFetcher;
    }

    //----------------------------------------------------------------------------------
    // Public Methods
    //----------------------------------------------------------------------------------
    public void requestTopStoriesSync() {
        Bundle settingsBundle = makeExpeditedManualSyncSettingsBundle();
        settingsBundle.putInt(HackerNewsSyncAdapter.EXTRA_KEY_LIMIT, 20);
        Log.d(TAG, "Requesting data sync for account: " + mAccount);
        ContentResolver.requestSync(mAccount, HackerNewsData.CONTENT_AUTHORITY, settingsBundle);
    }

    public void requestCommentsSync(Item item, int limit) {
        Bundle settingsBundle = makeExpeditedManualSyncSettingsBundle();
        settingsBundle.putInt(HackerNewsSyncAdapter.EXTRA_STORY, item.getId());
        settingsBundle.putInt(HackerNewsSyncAdapter.EXTRA_KEY_LIMIT, limit);
        ContentResolver.requestSync(mAccount, HackerNewsData.CONTENT_AUTHORITY, settingsBundle);
    }

    public boolean isSyncActive() {
        return ContentResolver.isSyncActive(mAccount, HackerNewsData.CONTENT_AUTHORITY);
    }

    public boolean isSyncPending() {
        return ContentResolver.isSyncPending(mAccount, HackerNewsData.CONTENT_AUTHORITY);
    }

    public boolean isSyncActiveOrPending() {
        return isSyncPending() || isSyncActive();
    }

    //----------------------------------------------------------------------------------
    // Package Private - Called by HackerNewsSyncAdapter
    //----------------------------------------------------------------------------------
    void onSyncComments(int limit, int storyId) {
        /*
        Unfortunately, because of the way SyncAdapters work, we couldn't pass a story into this method.
        So, we have to get the story from the cache. Right now, the cache fetcher only hits the database
        for cached data.

        TODO Consider using an LruCache to improve performance here.
         */
        Item story = mCachedDataFetcher.getStory(storyId);
        List<Item> commentsForStory = mRemoteDataFetcher.getCommentsForStory(story, limit);
        List<Item> cachedComments = mCachedDataFetcher.getCommentsForStory(story);
        syncDatabaseWithRemoteItems(cachedComments, commentsForStory, mHackerNewsCache);
        mHackerNewsCache.notifyCacheUpdated();
    }

    void onSyncStories(int limit) {
        List<Item> topStories = mRemoteDataFetcher.getTopStories(limit);
        List<Item> cachedTopStories = mCachedDataFetcher.getTopStories();
        syncDatabaseWithRemoteItems(cachedTopStories, topStories, mHackerNewsCache);
        mHackerNewsCache.notifyCacheUpdated();
    }

    //----------------------------------------------------------------------------------
    // Helpers
    //----------------------------------------------------------------------------------
    @NonNull
    private Bundle makeExpeditedManualSyncSettingsBundle() {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        return settingsBundle;
    }

    private void syncDatabaseWithRemoteItems(List<Item> cachedItems, List<Item> remoteItems,
                                             HackerNewsCache hackerNewsCache) {
        for (int i = 0; i < remoteItems.size(); i++) {
            Item item = remoteItems.get(i);
            if (cachedItems.contains(item)) {
                hackerNewsCache.updateItem(item);
            } else {
                hackerNewsCache.insertItem(item);
            }
        }
    }
}
