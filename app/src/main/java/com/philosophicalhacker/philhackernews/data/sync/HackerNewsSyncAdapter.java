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

import com.philosophicalhacker.philhackernews.data.HackerNewsDataSource;
import com.philosophicalhacker.philhackernews.data.content.HackerNewsData;
import com.philosophicalhacker.philhackernews.data.content.HackerNewsDataCache;
import com.philosophicalhacker.philhackernews.model.Story;

import java.util.List;

/**
 *
 * Created by MattDupree on 7/18/15.
 */
public class HackerNewsSyncAdapter extends AbstractThreadedSyncAdapter {

    private final HackerNewsDataSource mRemoteHackerNewsDataSource;
    private final HackerNewsDataCache mHackerNewsDataCache;

    public HackerNewsSyncAdapter(Context context, boolean autoInitialize,
                                 HackerNewsDataSource remoteHackerNewsDataSource,
                                 HackerNewsDataCache hackerNewsDataCache) {
        super(context, autoInitialize);
        mRemoteHackerNewsDataSource = remoteHackerNewsDataSource;
        mHackerNewsDataCache = hackerNewsDataCache;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        List<Integer> topStories = mRemoteHackerNewsDataSource.getTopStories();
        List<Integer> cachedTopStories = mHackerNewsDataCache.getTopStories();
        for (int i = 0; i < 20; i++) {
            Integer storyId = topStories.get(i);
            /*
            Unfortunately, the hackernews api doesn't return a list of stories. It only returns a list
            of ids that point to the top stories, so we have to make a separate network call for
            each story here.

            TODO Consider writing AppEngine based rest api that simply exposes HackerNews api in a more
            mobile friendly manner.
             */
            Story story = mRemoteHackerNewsDataSource.getStory(storyId);
            ContentValues contentValues = getContentValuesForStory(story);

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
            if (cachedTopStories.contains(storyId)) {
                contentResolver.update(ContentUris.withAppendedId(HackerNewsData.Stories.CONTENT_URI, storyId), contentValues, null, null);
            } else {
                contentResolver.insert(HackerNewsData.Stories.CONTENT_URI, contentValues);
            }
        }
    }

    //----------------------------------------------------------------------------------
    // Helpers
    //----------------------------------------------------------------------------------
    @NonNull
    private ContentValues getContentValuesForStory(Story story) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(HackerNewsData.Stories._ID, story.getId());
        contentValues.put(HackerNewsData.Stories.SCORE, story.getScore());
        return contentValues;
    }
}
