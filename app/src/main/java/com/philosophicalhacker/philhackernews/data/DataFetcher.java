package com.philosophicalhacker.philhackernews.data;

import com.philosophicalhacker.philhackernews.data.cache.CachedDataFetcher;
import com.philosophicalhacker.philhackernews.data.remote.RemoteDataFetcher;
import com.philosophicalhacker.philhackernews.model.Item;

import java.util.List;

/**
 * A source of HackerNews data. Both {@link CachedDataFetcher}
 * and {@link RemoteDataFetcher} implement this interface.
 *
 * Created by MattDupree on 7/18/15.
 */
public interface DataFetcher {
    List<Item> getTopStories(int limit);

    List<Item> getTopStories();

    Item getStory(int storyId);

    Item getComment(int commentId);
}
