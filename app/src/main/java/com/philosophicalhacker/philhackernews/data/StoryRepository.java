package com.philosophicalhacker.philhackernews.data;

import com.philosophicalhacker.philhackernews.model.Story;

import java.util.List;

import rx.Observable;

/**
 * Created by MattDupree on 7/16/15.
 */
public interface StoryRepository {

    Observable<List<Story>> getTopStoriesObservable(int page, int count);
}
