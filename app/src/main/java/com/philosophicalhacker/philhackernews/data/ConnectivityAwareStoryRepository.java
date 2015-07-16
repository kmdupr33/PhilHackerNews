package com.philosophicalhacker.philhackernews.data;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.observables.ConnectableObservable;

/**
 * Created by MattDupree on 7/16/15.
 */
public class ConnectivityAwareStoryRepository implements StoryRepository {

    private ConnectableObservable<List<Integer>> mStoriesObservable;

    public ConnectivityAwareStoryRepository(ConnectableObservable<List<Integer>> apiStoriesObservable) {
        mStoriesObservable = apiStoriesObservable;
    }

    @Override
    public Subscription addStoriesSubscriber(Subscriber<List<Integer>> observer) {
        return mStoriesObservable.subscribe(observer);
    }

    @Override
    public void loadTopStories() {
        mStoriesObservable.connect();
    }
}
