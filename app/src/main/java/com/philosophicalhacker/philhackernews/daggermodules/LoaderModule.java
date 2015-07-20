package com.philosophicalhacker.philhackernews.daggermodules;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;

import com.philosophicalhacker.philhackernews.data.DataConverter;
import com.philosophicalhacker.philhackernews.data.LoaderInitializingOnSubscribe;
import com.philosophicalhacker.philhackernews.data.MultiCastingStoryRepository;
import com.philosophicalhacker.philhackernews.data.StoryRepository;
import com.philosophicalhacker.philhackernews.model.Story;
import com.philosophicalhacker.philhackernews.ui.MainActivityFragment;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
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
    ConnectableObservable<List<Story>> provideApiStoriesObservable(LoaderManager loaderManager,
                                                                   CursorLoader storyLoader,
                                                                     DataConverter<List<Story>, Cursor> cursorDataConverter) {
        return Observable.create(new LoaderInitializingOnSubscribe<>(API_STORY_LOADER, loaderManager, storyLoader, cursorDataConverter))
                .observeOn(AndroidSchedulers.mainThread())
                .publish();
    }


    @Provides
    StoryRepository provideStoryRepository(ConnectableObservable<List<Story>> apiStoriesObservable) {
        return new MultiCastingStoryRepository(apiStoriesObservable);
    }

}
