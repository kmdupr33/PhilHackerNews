package com.philosophicalhacker.philhackernews.data;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by MattDupree on 7/16/15.
 */
public interface StoryRepository {

    Subscription addStoriesSubscriber(Subscriber<List<Integer>> observer);

    void loadTopStories();
}
