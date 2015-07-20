package com.philosophicalhacker.philhackernews.data.sync;

import android.accounts.Account;
import android.content.ContentResolver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philosophicalhacker.philhackernews.data.cache.HackerNewsData;
import com.philosophicalhacker.philhackernews.model.Item;

import javax.inject.Inject;

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
        Bundle settingsBundle = makeExpenditedManualSyncSettingsBundle();
        settingsBundle.putInt(HackerNewsSyncAdapter.EXTRA_KEY_LIMIT, 20);
        Log.d(TAG, "Requesting data sync for account: " + mAccount);
        ContentResolver.requestSync(mAccount, HackerNewsData.CONTENT_AUTHORITY, settingsBundle);
    }

    public void requestCommentsSync(Item item) {
        Bundle settingsBundle = makeExpenditedManualSyncSettingsBundle();
        settingsBundle.putIntArray(HackerNewsSyncAdapter.EXTRA_COMMENTS, item.getComments());
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
    // Helpers
    //----------------------------------------------------------------------------------
    @NonNull
    private Bundle makeExpenditedManualSyncSettingsBundle() {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        return settingsBundle;
    }
}
