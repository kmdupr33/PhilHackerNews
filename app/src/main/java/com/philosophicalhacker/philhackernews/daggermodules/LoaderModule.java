package com.philosophicalhacker.philhackernews.daggermodules;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.philosophicalhacker.philhackernews.MainActivityFragment;
import com.philosophicalhacker.philhackernews.data.ConnectivityAwareStoryRepository;
import com.philosophicalhacker.philhackernews.data.HackerNewsRestAdapter;
import com.philosophicalhacker.philhackernews.data.StoryLoader;
import com.philosophicalhacker.philhackernews.data.StoryRepository;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import rx.Observable;
import rx.Subscriber;
import rx.observables.ConnectableObservable;

/**
 * Created by MattDupree on 7/16/15.
 */
@Module(injects = MainActivityFragment.class, addsTo = PhilHackerNewsAppModule.class)
public class LoaderModule {

    private static final int API_STORY_LOADER = 0;
    private Fragment mFragment;

    public LoaderModule(Fragment fragment) {
        mFragment = fragment;
    }

    @Provides
    LoaderManager provideLoaderManager() {
        return mFragment.getLoaderManager();
    }

    @Provides
    ConnectableObservable<List<Integer>> provideApiStoriesObservable(final LoaderManager loaderManager,
                                                                   final Context context,
                                                                   final HackerNewsRestAdapter hackerNewsRestAdapter) {
        return Observable.create(new Observable.OnSubscribe<List<Integer>>() {
            @Override
            public void call(final Subscriber<? super List<Integer>> subscriber) {
                loaderManager.initLoader(API_STORY_LOADER, null, new LoaderManager.LoaderCallbacks<List<Integer>>() {
                    @Override
                    public Loader<List<Integer>> onCreateLoader(int id, Bundle args) {
                        return new StoryLoader(context, hackerNewsRestAdapter);
                    }

                    @Override
                    public void onLoadFinished(Loader<List<Integer>> loader, List<Integer> data) {
                        subscriber.onNext(data);
                    }

                    @Override
                    public void onLoaderReset(Loader<List<Integer>> loader) {

                    }
                });
            }
        }).publish();
    }

    @Provides
    StoryRepository provideStoryRepository(ConnectableObservable<List<Integer>> storiesObservable) {
        return new ConnectivityAwareStoryRepository(storiesObservable);
    }
}
