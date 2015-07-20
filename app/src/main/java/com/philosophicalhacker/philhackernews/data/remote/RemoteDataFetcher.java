package com.philosophicalhacker.philhackernews.data.remote;

import com.philosophicalhacker.philhackernews.data.DataFetcher;
import com.philosophicalhacker.philhackernews.model.Item;

import java.util.ArrayList;
import java.util.List;

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
    public List<Item> getTopStories(int limit) {
        /*
        This data fetcher has to be responsible for limiting the stories returned by the RestAdapter
        because the firebase api doesn't allow us to limit the data we query for without also sorting
        the order of the data that we're querying. Since the data is already sorted according to popularity,
        we don't want to mess that up.
         */
        List<Integer> unlimitedTopStories = mHackerNewsRestAdapter.getTopStories();
        List<Item> limitedTopStories;
        if (limit == Integer.MAX_VALUE) {
            limitedTopStories = new ArrayList<>();
        } else {
            limitedTopStories = new ArrayList<>(limit);
        }
        int end = limit == Integer.MAX_VALUE ? unlimitedTopStories.size() : limit;
        for (int i = 0; i < end; i++) {
            /*
            Unfortunately, the hackernews api doesn't return a list of stories. It only returns a list
            of ids that point to the top stories, so we have to make a separate network call for
            each story here.

            TODO Consider writing AppEngine based rest api that simply exposes HackerNews api in a more
            mobile friendly manner.
             */
            Item item = getStory(unlimitedTopStories.get(i));
            limitedTopStories.add(item);
        }
        return limitedTopStories;
    }

    @Override
    public List<Item> getTopStories() {
        return getTopStories(Integer.MAX_VALUE);
    }

    @Override
    public Item getStory(int storyId) {
        return mHackerNewsRestAdapter.getStory(storyId);
    }
}
