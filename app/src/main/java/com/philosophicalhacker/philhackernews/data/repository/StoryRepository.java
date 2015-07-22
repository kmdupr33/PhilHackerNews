package com.philosophicalhacker.philhackernews.data.repository;

import com.philosophicalhacker.philhackernews.model.Item;

import java.util.List;

import rx.Observable;

/**
 * Used by Activities and Fragments to abstract away concerns about whether they're loading data from the Cache or from the Network
 *
 * Created by MattDupree on 7/16/15.
 */
public interface StoryRepository {
    Observable<List<Item>> loadTopStories();
}
