package com.philosophicalhacker.philhackernews.data;

/**
 *
 * @param <T> the target type of the conversion process represented by this converter
 * @param <U> the type of the source object to be converted
 */
public interface DataConverter<T, U> {
    T convertData(U data);
}
