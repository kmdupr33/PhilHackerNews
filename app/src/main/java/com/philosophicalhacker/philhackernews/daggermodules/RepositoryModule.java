package com.philosophicalhacker.philhackernews.daggermodules;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;

import com.philosophicalhacker.philhackernews.data.DataConverter;
import com.philosophicalhacker.philhackernews.data.ItemRepository;
import com.philosophicalhacker.philhackernews.data.LoaderInitializingOnSubscribe;
import com.philosophicalhacker.philhackernews.data.cache.HackerNewsData;
import com.philosophicalhacker.philhackernews.data.repository.CacheOnlyCommentRepository;
import com.philosophicalhacker.philhackernews.data.repository.CacheOnlyStoryRepository;
import com.philosophicalhacker.philhackernews.data.repository.CommentRepository;
import com.philosophicalhacker.philhackernews.data.repository.StoryRepository;
import com.philosophicalhacker.philhackernews.model.Item;
import com.philosophicalhacker.philhackernews.ui.commentslist.CommentsFragment;
import com.philosophicalhacker.philhackernews.ui.storieslist.StoriesFragment;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Provides dependencies required for Loading HackerNews Data
 *
 * Created by MattDupree on 7/16/15.
 */
@Module(injects = {StoriesFragment.class,
        CommentsFragment.class},
        addsTo = PhilHackerNewsAppModule.class,
        complete = false)
public class RepositoryModule {

    private static final int API_STORY_LOADER = 0;

    private LoaderManager mLoaderManager;

    public RepositoryModule(LoaderManager loaderManager) {
        mLoaderManager = loaderManager;
    }

    @Provides
    LoaderManager provideLoaderManager() {
        return mLoaderManager;
    }

    @Provides
    CursorLoader provideStoryLoader(Context context) {
        return new CursorLoader(context, HackerNewsData.Items.CONTENT_URI, null,
                HackerNewsData.Items.TYPE + "= ?", new String[] {Item.TYPE_STORY}, ItemRepository.SCORE_DESC_SORT_ORDER);
    }

    @Provides
    Observable<List<Item>> provideApiStoriesObservable(LoaderManager loaderManager,
                                                                   CursorLoader storyLoader,
                                                                     DataConverter<List<Item>, Cursor> cursorDataConverter) {
        return Observable.create(new LoaderInitializingOnSubscribe<>(API_STORY_LOADER, loaderManager, storyLoader, cursorDataConverter))
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Provides
    StoryRepository provideStoryRepository(Observable<List<Item>> apiStoriesObservable) {
        return new CacheOnlyStoryRepository(apiStoriesObservable);
    }

    @Provides
    CommentRepository provideCommentRepository(LoaderManager loaderManager, Context context,
                                               DataConverter<List<Item>, Cursor> dataConverter) {
        return new CacheOnlyCommentRepository(loaderManager, context, dataConverter);
    }

}
