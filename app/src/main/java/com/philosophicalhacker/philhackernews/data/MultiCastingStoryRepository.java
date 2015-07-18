package com.philosophicalhacker.philhackernews.data;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.observables.ConnectableObservable;

/**
 * StoryRepository that allows multiple RxJava subscribers to subscribe to data loading events.
 *
 * Created by MattDupree on 7/16/15.
 */
public class MultiCastingStoryRepository implements StoryRepository {

    private ConnectableObservable<List<Integer>> mStoriesObservable;

    public MultiCastingStoryRepository(ConnectableObservable<List<Integer>> storiesObservable) {
        mStoriesObservable = storiesObservable;
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
