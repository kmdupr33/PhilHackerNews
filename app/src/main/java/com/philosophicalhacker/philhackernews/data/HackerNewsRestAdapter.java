package com.philosophicalhacker.philhackernews.data;

import java.util.List;

import retrofit.http.GET;

/**
 * Created by MattDupree on 7/16/15.
 */
public interface HackerNewsRestAdapter {

    @GET("/topstories.json")
    List<Integer> getTopStories();
}
