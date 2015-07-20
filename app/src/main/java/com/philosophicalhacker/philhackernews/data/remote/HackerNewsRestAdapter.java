package com.philosophicalhacker.philhackernews.data.remote;

import com.philosophicalhacker.philhackernews.model.Item;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by MattDupree on 7/16/15.
 */
public interface HackerNewsRestAdapter {

    @GET("/topstories.json")
    List<Integer> getTopStories();

    @GET("/item/{id}.json")
    Item getStory(@Path("id") int id);
}
