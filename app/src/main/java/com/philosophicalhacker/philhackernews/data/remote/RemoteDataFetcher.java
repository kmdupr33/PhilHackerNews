package com.philosophicalhacker.philhackernews.data.remote;

import com.philosophicalhacker.philhackernews.data.DataFetcher;
import com.philosophicalhacker.philhackernews.model.Story;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 *
 * Created by MattDupree on 7/18/15.
 */
public class RemoteDataFetcher implements DataFetcher {
    public static final String DAGGER_INJECT_QUALIFIER = "remote";

    private HackerNewsRestAdapter mHackerNewsRestAdapter;

    public RemoteDataFetcher(HackerNewsRestAdapter hackerNewsRestAdapter) {
        mHackerNewsRestAdapter = hackerNewsRestAdapter;
    }

    @Override
    public List<Integer> getTopStories(int limit) {
        /*
        This data fetcher has to be responsible for limiting the stories returned by the RestAdapter
        because the firebase api doesn't allow us to limit the data we query for without also sorting
        the order of the data that we're querying. Since the data is already sorted according to popularity,
        we don't want to mess that up.
         */
        List<Integer> unlimitedTopStories = mHackerNewsRestAdapter.getTopStories();
        List<Integer> limitedTopStories;
        if (limit == Integer.MAX_VALUE) {
            limitedTopStories = new ArrayList<>();
        } else {
            limitedTopStories = new ArrayList<>(limit);
        }
        int end = limit == Integer.MAX_VALUE ? unlimitedTopStories.size() : limit;
        for (int i = 0; i < end; i++) {
            limitedTopStories.add(unlimitedTopStories.get(i));
        }
        return limitedTopStories;
    }

    @Override
    public List<Integer> getTopStories() {
        return null;
    }

    @Override
    public Story getStory(Integer storyId) {
        return mHackerNewsRestAdapter.getStory(storyId);
    }
}
