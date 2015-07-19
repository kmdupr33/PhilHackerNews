package com.philosophicalhacker.philhackernews.data;

import com.philosophicalhacker.philhackernews.data.cache.CachedDataFetcher;
import com.philosophicalhacker.philhackernews.data.remote.RemoteDataFetcher;
import com.philosophicalhacker.philhackernews.model.Story;

import java.util.List;

/**
 * A source of HackerNews data. Both {@link CachedDataFetcher}
 * and {@link RemoteDataFetcher} implement this interface.
 *
 * Created by MattDupree on 7/18/15.
 */
public interface DataFetcher {
    List<Story> getTopStories(int limit);

    List<Story> getTopStories();

    Story getStory(int storyId);
}
