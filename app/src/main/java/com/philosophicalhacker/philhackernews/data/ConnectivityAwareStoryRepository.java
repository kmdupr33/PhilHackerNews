package com.philosophicalhacker.philhackernews.data;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.observables.ConnectableObservable;

/**
 * Created by MattDupree on 7/16/15.
 */
public class ConnectivityAwareStoryRepository implements StoryRepository {

    private ConnectableObservable<List<Integer>> mRemoteStoriesObservable;
    private ConnectivityManager mConnectivityManager;
    private ConnectableObservable<List<Integer>> mCachedStoriesObservable;

    public ConnectivityAwareStoryRepository(ConnectableObservable<List<Integer>> apiStoriesObservable,
                                            ConnectableObservable<List<Integer>> cachedStoriesObservable,
                                            ConnectivityManager connectivityManager) {
        mRemoteStoriesObservable = apiStoriesObservable;
        mConnectivityManager = connectivityManager;
        mCachedStoriesObservable = cachedStoriesObservable;
    }

    @Override
    public Subscription addStoriesSubscriber(Subscriber<List<Integer>> observer) {
        return mRemoteStoriesObservable.subscribe(observer);
    }

    @Override
    public void loadTopStories() {
        if (isConnected()) {
            mRemoteStoriesObservable.connect();
        } else {
            mCachedStoriesObservable.connect();
        }
    }

    //----------------------------------------------------------------------------------
    // Helpers
    //----------------------------------------------------------------------------------
    private boolean isConnected() {
        NetworkInfo activeNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
