package com.philosophicalhacker.philhackernews.data;

import com.philosophicalhacker.philhackernews.model.Item;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by MattDupree on 7/16/15.
 */
public interface StoryRepository {

    Subscription addStoriesSubscriber(Subscriber<List<Item>> storiesSubscriber);

    void loadTopStories();
}
