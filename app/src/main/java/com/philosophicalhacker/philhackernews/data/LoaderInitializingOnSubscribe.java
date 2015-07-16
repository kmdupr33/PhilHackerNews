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
public class LoaderInitializingOnSubscribe<T> implements Observable.OnSubscribe<T> {
    private int mLoadId;
    private final LoaderManager mLoaderManager;
    private Loader<T> mLoader;

    public LoaderInitializingOnSubscribe(int loadId, LoaderManager loaderManager, Loader<T> loader) {
        mLoadId = loadId;
        mLoaderManager = loaderManager;
        mLoader = loader;
    }

    @Override
    public void call(final Subscriber<? super T> subscriber) {
        mLoaderManager.initLoader(mLoadId, null, new LoaderManager.LoaderCallbacks<T>() {
            @Override
            public Loader<T> onCreateLoader(int id, Bundle args) {
                return mLoader;
            }

            @Override
            public void onLoadFinished(Loader<T> loader, T data) {
                subscriber.onNext(data);
            }

            @Override
            public void onLoaderReset(Loader<T> loader) {
            }
        });
    }
}
