package com.philosophicalhacker.philhackernews.daggermodules;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;

import com.philosophicalhacker.philhackernews.data.LoaderDataConverter;
import com.philosophicalhacker.philhackernews.data.LoaderInitializingOnSubscribe;
import com.philosophicalhacker.philhackernews.data.MultiCastingStoryRepository;
import com.philosophicalhacker.philhackernews.data.StoryRepository;
import com.philosophicalhacker.philhackernews.ui.MainActivityFragment;

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

    @Provides
    ConnectableObservable<List<Integer>> provideApiStoriesObservable(LoaderManager loaderManager,
                                                                   CursorLoader storyLoader,
                                                                     LoaderDataConverter<List<Integer>, Cursor> cursorLoaderDataConverter) {
        return Observable.create(new LoaderInitializingOnSubscribe<>(API_STORY_LOADER, loaderManager, storyLoader, cursorLoaderDataConverter)).publish();
    }


    @Provides
    StoryRepository provideStoryRepository(ConnectableObservable<List<Integer>> apiStoriesObservable) {
        return new MultiCastingStoryRepository(apiStoriesObservable);
    }

}
