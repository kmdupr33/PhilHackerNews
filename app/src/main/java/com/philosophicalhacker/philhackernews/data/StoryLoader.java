package com.philosophicalhacker.philhackernews.data;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by MattDupree on 7/16/15.
 */
public class StoryLoader extends AsyncTaskLoader<List<Integer>> {
    private final HackerNewsRestAdapter mHackerNewsRestAdapter;
    private List<Integer> mTopStories;

    /**
     * Stores away the application context associated with context. Since Loaders can be used
     * across multiple activities it's dangerous to store the context directly.
     *
     * @param context used to retrieve the application context.
     * @param hackerNewsRestAdapter
     */
    public StoryLoader(Context context, HackerNewsRestAdapter hackerNewsRestAdapter) {
        super(context);
        mHackerNewsRestAdapter = hackerNewsRestAdapter;
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
        mTopStories = mHackerNewsRestAdapter.getTopStories();
        return mTopStories;
    }
}
