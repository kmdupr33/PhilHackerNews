package com.philosophicalhacker.philhackernews.data;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by MattDupree on 7/16/15.
 */
public class StoryLoader extends AsyncTaskLoader<List<Integer>> {
    private final HackerNewsDataSource mHackerNewsDataSource;
    private List<Integer> mTopStories;

    /**
     * Stores away the application context associated with context. Since Loaders can be used
     * across multiple activities it's dangerous to store the context directly.
     *
     * @param context used to retrieve the application context.
     * @param hackerNewsDataSource
     */
    public StoryLoader(Context context, HackerNewsDataSource hackerNewsDataSource) {
        super(context);
        mHackerNewsDataSource = hackerNewsDataSource;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mTopStories != null) {
            deliverResult(mTopStories);
        } else {
            forceLoad();
        }
    }

    @Override
    public List<Integer> loadInBackground() {
        mTopStories = mHackerNewsDataSource.getTopStories();
        return mTopStories;
    }
}
