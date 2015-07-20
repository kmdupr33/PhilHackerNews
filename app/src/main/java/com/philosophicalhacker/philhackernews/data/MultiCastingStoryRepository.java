package com.philosophicalhacker.philhackernews.data;

import com.philosophicalhacker.philhackernews.model.Item;

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

    private ConnectableObservable<List<Item>> mStoriesObservable;

    public MultiCastingStoryRepository(ConnectableObservable<List<Item>> storiesObservable) {
        mStoriesObservable = storiesObservable;
    }

    @Override
    public Subscription addStoriesSubscriber(Subscriber<List<Item>> storiesSubscriber) {
        return mStoriesObservable.subscribe(storiesSubscriber);
    }

    @Override
    public void loadTopStories() {
        mStoriesObservable.connect();
    }
}
