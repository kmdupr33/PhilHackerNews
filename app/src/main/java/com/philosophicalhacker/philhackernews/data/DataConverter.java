package com.philosophicalhacker.philhackernews.data;

/**
 * Created by MattDupree on 7/18/15.
 */
public interface DataConverter<T, U> {
    T convertData(U data);
}
