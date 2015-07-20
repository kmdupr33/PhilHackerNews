package com.philosophicalhacker.philhackernews.data;

import com.philosophicalhacker.philhackernews.model.Item;

import java.util.List;

import rx.Observable;

/**
 * Created by MattDupree on 7/20/15.
 */
public interface CommentRepository extends ItemRepository {
    Observable<List<Item>> loadCommentsForStory(Item item);
}
