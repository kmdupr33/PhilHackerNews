package com.philosophicalhacker.philhackernews.data.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.philosophicalhacker.philhackernews.data.HackerNewsDataSource;
import com.philosophicalhacker.philhackernews.data.content.HackerNewsData;
import com.philosophicalhacker.philhackernews.model.Story;

import java.util.List;

/**
 * Created by MattDupree on 7/18/15.
 */
public class HackerNewsSyncAdapter extends AbstractThreadedSyncAdapter {

    private final HackerNewsDataSource mHackerNewsDataSource;

    public HackerNewsSyncAdapter(Context context, boolean autoInitialize,
                                 HackerNewsDataSource hackerNewsDataSource) {
        super(context, autoInitialize);
        mHackerNewsDataSource = hackerNewsDataSource;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        List<Integer> topStories = mHackerNewsDataSource.getTopStories();
        ContentValues[] contentValuesArray = new ContentValues[topStories.size()];
        for (int i = 0; i < 20; i++) {
            Integer storyId = topStories.get(i);
            /*
            Unfortunately, the hackernews api doesn't return a list of stories. It only returns a list
            of ids that point to the top stories, so we have to make a separate network call for
            each story here.

            TODO Consider writing AppEngine based rest api that simply exposes HackerNews api in a more
            mobile friendly manner.
             */
            Story story = mHackerNewsDataSource.getStory(storyId);
            ContentValues contentValues = new ContentValues();
            contentValues.put(HackerNewsData.Stories._ID, story.getId());
            contentValues.put(HackerNewsData.Stories.SCORE, story.getScore());
            contentValuesArray[i] = contentValues;
        }
        getContext().getContentResolver().bulkInsert(HackerNewsData.Stories.CONTENT_URI, contentValuesArray);
    }
}
