package com.philosophicalhacker.philhackernews.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;

import com.philosophicalhacker.philhackernews.data.cache.HackerNewsData;
import com.philosophicalhacker.philhackernews.data.sync.DataSynchronizer;
import com.philosophicalhacker.philhackernews.model.Story;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * StoryRepository that checks the cache first before making an API call.
 *
 * Created by MattDupree on 7/16/15.
 */
public class CacheFirstStoryRepository implements StoryRepository {

    private static final int API_STORY_LOADER = 0;

    private final LoaderManager mLoaderManager;
    private final DataSynchronizer mDataSynchronizer;
    private Context mContext;
    private DataConverter<List<Story>, Cursor> mDataConverter;

    public CacheFirstStoryRepository(PaginatedDataLoaderManager loaderManager,
                                     DataSynchronizer dataSynchronizer,
                                     Context context,
                                     DataConverter<List<Story>, Cursor> dataConverter) {
        mLoaderManager = loaderManager;
        mDataSynchronizer = dataSynchronizer;
        mContext = context;
        mDataConverter = dataConverter;
    }

    @Override
    public Observable<List<Story>> getTopStoriesObservable(int page, int count) {
        LoaderInitializingOnSubscribe<List<Story>, Cursor> loaderInitializingOnSubscribe = makeLoaderInitializingOnSubscribe(page,
                                                                                                                             count);
        final SyncRequestingOnSubscribe syncRequestingOnSubscribe = new SyncRequestingOnSubscribe();
        return Observable.create(loaderInitializingOnSubscribe).flatMap(
                new Func1<List<Story>, Observable<List<Story>>>() {
                    @Override
                    public Observable<List<Story>> call(List<Story> stories) {
                        if (stories.size() > 0) {
                            return Observable.just(stories);
                        } else {
                            return Observable.create(syncRequestingOnSubscribe);
                        }
                    }
                });
    }

    @NonNull
    private LoaderInitializingOnSubscribe<List<Story>, Cursor> makeLoaderInitializingOnSubscribe(int page, int count) {
        Uri.Builder builder = HackerNewsData.Stories.CONTENT_URI.buildUpon();
        builder.appendQueryParameter("page", String.valueOf(page))
                .appendQueryParameter("count", String.valueOf(count));
        Uri uri = builder.build();
        PaginatedCursorLoader cursorLoader = new PaginatedCursorLoader(mContext, uri, null, null, null, null);
        return new LoaderInitializingOnSubscribe<>(API_STORY_LOADER, mLoaderManager, cursorLoader, mDataConverter);
    }
}
