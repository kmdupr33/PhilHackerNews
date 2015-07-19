package com.philosophicalhacker.philhackernews.data.sync;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.util.Log;

import com.philosophicalhacker.philhackernews.data.cache.HackerNewsData;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Makes requests to synchronize cached device data with remote api data.
 *
 * Created by MattDupree on 7/19/15.
 */
public class DataSynchronizer {

    private static final String TAG = DataSynchronizer.class.getSimpleName();

    private Account mAccount;

    @Inject
    public DataSynchronizer(Account account) {
        mAccount = account;
    }

    /**
     *
     * @return an observable that reports changes in the sync request status. Possible emited values are
     * {@link ContentResolver#SYNC_OBSERVER_TYPE_ACTIVE}, {@link ContentResolver#SYNC_OBSERVER_TYPE_PENDING},
     * and {@link ContentResolver#SYNC_OBSERVER_TYPE_SETTINGS}
     */
    public void requestTopStoriesSync() {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        settingsBundle.putInt(HackerNewsSyncAdapter.EXTRA_KEY_TOP_STORIES_LIMIT, 20);
        Log.d(TAG, "Requesting data sync for account: " + mAccount);
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

}
