package com.philosophicalhacker.philhackernews.daggermodules;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.LoaderManager;

import com.philosophicalhacker.philhackernews.ui.MainActivityFragment;
import com.philosophicalhacker.philhackernews.data.ConnectivityAwareStoryRepository;
import com.philosophicalhacker.philhackernews.data.HackerNewsRestAdapter;
import com.philosophicalhacker.philhackernews.data.LoaderInitializingOnSubscribe;
import com.philosophicalhacker.philhackernews.data.StoryLoader;
import com.philosophicalhacker.philhackernews.data.StoryRepository;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import rx.Observable;
import rx.observables.ConnectableObservable;

/**
 * Provides dependencies required for Loading HackerNews Data
 *
 * Created by MattDupree on 7/16/15.
 */
@Module(injects = MainActivityFragment.class, addsTo = PhilHackerNewsAppModule.class)
public class LoaderModule {

    private static final int API_STORY_LOADER = 0;
    private LoaderManager mLoaderManager;

    public LoaderModule(LoaderManager loaderManager) {
        mLoaderManager = loaderManager;
    }

    @Provides
    LoaderManager provideLoaderManager() {
        return mLoaderManager;
    }

    @Provides
    ConnectableObservable<List<Integer>> provideApiStoriesObservable(final LoaderManager loaderManager,
                                                                   final Context context,
                                                                   final HackerNewsRestAdapter hackerNewsRestAdapter) {
        StoryLoader storyLoader = new StoryLoader(context, hackerNewsRestAdapter);
        return Observable.create(new LoaderInitializingOnSubscribe<>(API_STORY_LOADER, loaderManager, storyLoader)).publish();
    }

    @Provides
    StoryRepository provideStoryRepository(ConnectableObservable<List<Integer>> storiesObservable,
                                           ConnectivityManager connectivityManager) {
        return new ConnectivityAwareStoryRepository(storiesObservable, connectivityManager);
    }

}
