package com.philosophicalhacker.philhackernews.data.repository;

import com.philosophicalhacker.philhackernews.model.Item;

import java.util.List;

import rx.Observable;

/**
 * StoryRepository that allows multiple RxJava subscribers to subscribe to data loading events.
 *
 * Created by MattDupree on 7/16/15.
 */
public class CacheOnlyStoryRepository implements StoryRepository {

    private Observable<List<Item>> mStoriesObservable;

    public CacheOnlyStoryRepository(Observable<List<Item>> storiesObservable) {
        mStoriesObservable = storiesObservable;
    }

    @Override
    public Observable<List<Item>> loadTopStories() {
        return mStoriesObservable;
    }
}
