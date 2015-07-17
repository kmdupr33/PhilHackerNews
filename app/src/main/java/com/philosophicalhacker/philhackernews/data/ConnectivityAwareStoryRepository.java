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

    private ConnectableObservable<List<Integer>> mStoriesObservable;
    private ConnectivityManager mConnectivityManager;

    public ConnectivityAwareStoryRepository(ConnectableObservable<List<Integer>> apiStoriesObservable,
                                            ConnectivityManager connectivityManager) {
        mStoriesObservable = apiStoriesObservable;
        mConnectivityManager = connectivityManager;
    }

    @Override
    public Subscription addStoriesSubscriber(Subscriber<List<Integer>> observer) {
        return mStoriesObservable.subscribe(observer);
    }

    @Override
    public void loadTopStories() {
        if (isConnected()) {
            mStoriesObservable.connect();
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
