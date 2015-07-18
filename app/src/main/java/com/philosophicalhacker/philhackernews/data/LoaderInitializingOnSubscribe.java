package com.philosophicalhacker.philhackernews.data;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import rx.Observable;
import rx.Subscriber;

/**
 *
 * Created by MattDupree on 7/16/15.
 */
public class LoaderInitializingOnSubscribe<T, U> implements Observable.OnSubscribe<T> {
    private int mLoadId;
    private final LoaderManager mLoaderManager;
    private Loader<U> mLoader;
    private LoaderDataConverter<T, U> mLoaderDataConverter;

    public LoaderInitializingOnSubscribe(int loadId,
                                         LoaderManager loaderManager,
                                         Loader<U> loader,
                                         LoaderDataConverter<T, U> loaderDataConverter) {
        mLoadId = loadId;
        mLoaderManager = loaderManager;
        mLoader = loader;
        mLoaderDataConverter = loaderDataConverter;
    }

    @Override
    public void call(final Subscriber<? super T> subscriber) {
        mLoaderManager.initLoader(mLoadId, null, new LoaderManager.LoaderCallbacks<U>() {
            @Override
            public Loader<U> onCreateLoader(int id, Bundle args) {
                return mLoader;
            }

            @Override
            public void onLoadFinished(Loader<U> loader, U data) {
                subscriber.onNext(mLoaderDataConverter.convertLoaderData(data));
            }

            @Override
            public void onLoaderReset(Loader<U> loader) {
            }
        });
    }
}
