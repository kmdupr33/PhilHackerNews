package com.philosophicalhacker.philhackernews.daggermodules;

import android.net.ConnectivityManager;
import android.support.v4.app.LoaderManager;

import com.philosophicalhacker.philhackernews.ui.MainActivityFragment;
import com.philosophicalhacker.philhackernews.data.ConnectivityAwareStoryRepository;
import com.philosophicalhacker.philhackernews.data.LoaderInitializingOnSubscribe;
import com.philosophicalhacker.philhackernews.data.StoryLoader;
import com.philosophicalhacker.philhackernews.data.StoryRepository;

import java.util.List;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.Observable;
import rx.observables.ConnectableObservable;

/**
 * Provides dependencies required for Loading HackerNews Data
 *
 * Created by MattDupree on 7/16/15.
 */
@Module(injects = MainActivityFragment.class, addsTo = PhilHackerNewsAppModule.class, complete = false)
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

    @Provides @Named("remote")
    ConnectableObservable<List<Integer>> provideApiStoriesObservable(LoaderManager loaderManager,
                                                                   @Named("remote") StoryLoader storyLoader) {
        return Observable.create(new LoaderInitializingOnSubscribe<>(API_STORY_LOADER, loaderManager, storyLoader)).publish();
    }

    @Provides @Named("cached")
    ConnectableObservable<List<Integer>> provideCachedStoriesObservable(LoaderManager loaderManager,
                                                                        @Named("cached") StoryLoader storyLoader) {
        return Observable.create(new LoaderInitializingOnSubscribe<>(API_STORY_LOADER, loaderManager, storyLoader)).publish();
    }

    @Provides
    StoryRepository provideStoryRepository(@Named("remote") ConnectableObservable<List<Integer>> apiStoriesObservable,
                                           @Named("cached") ConnectableObservable<List<Integer>> cachedStoriesObservable,
                                           ConnectivityManager connectivityManager) {
        return new ConnectivityAwareStoryRepository(apiStoriesObservable, cachedStoriesObservable, connectivityManager);
    }

}
