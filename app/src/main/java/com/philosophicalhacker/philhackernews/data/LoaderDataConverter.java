package com.philosophicalhacker.philhackernews.data;

/**
 * Created by MattDupree on 7/18/15.
 */
public interface LoaderDataConverter<T, U> {
    T convertLoaderData(U data);
}
