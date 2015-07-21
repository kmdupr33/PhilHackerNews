package com.philosophicalhacker.philhackernews.data.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

/**
 *
 * Created by MattDupree on 7/18/15.
 */
public class HackerNewsSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String EXTRA_KEY_LIMIT = "EXTRA_KEY_LIMIT";
    private static final String TAG = HackerNewsSyncAdapter.class.getSimpleName();
    public static final String EXTRA_STORY = "EXTRA_STORY";
    private static final int INVALID_VALUE = Integer.MIN_VALUE;
    private final DataSynchronizer mDataSynchronizer;

    public HackerNewsSyncAdapter(Context context, boolean autoInitialize,
                                 DataSynchronizer dataSynchronizer) {
        super(context, autoInitialize);
        mDataSynchronizer = dataSynchronizer;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "Performing data sync for account: " + account);
        int limit = extras.getInt(EXTRA_KEY_LIMIT, Integer.MAX_VALUE);
        int storyId = extras.getInt(EXTRA_STORY, INVALID_VALUE);
        if (storyId == INVALID_VALUE) {
            mDataSynchronizer.onSyncStories(limit);
        } else {
            mDataSynchronizer.onSyncComments(limit, storyId);
        }
    }
}
