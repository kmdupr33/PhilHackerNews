package com.philosophicalhacker.philhackernews.daggermodules;

import android.support.v4.app.LoaderManager;

import com.philosophicalhacker.philhackernews.data.CacheFirstStoryRepository;
import com.philosophicalhacker.philhackernews.data.StoryRepository;
import com.philosophicalhacker.philhackernews.model.Story;
import com.philosophicalhacker.philhackernews.ui.MainActivityFragment;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import rx.observables.ConnectableObservable;

/**
 * Provides dependencies required for Loading HackerNews Data
 *
 * Created by MattDupree on 7/16/15.
 */
@Module(injects = MainActivityFragment.class, addsTo = PhilHackerNewsAppModule.class, complete = false)
public class LoaderModule {
    private LoaderManager mLoaderManager;

    public LoaderModule(LoaderManager loaderManager) {
        mLoaderManager = loaderManager;
    }

    @Provides
    LoaderManager provideLoaderManager() {
        return mLoaderManager;
    }

    @Provides
    StoryRepository provideStoryRepository(ConnectableObservable<List<Story>> apiStoriesObservable) {
        return new CacheFirstStoryRepository(apiStoriesObservable);
    }

}
