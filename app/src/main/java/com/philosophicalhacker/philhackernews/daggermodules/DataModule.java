package com.philosophicalhacker.philhackernews.daggermodules;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.philosophicalhacker.philhackernews.data.CursorToItemConverter;
import com.philosophicalhacker.philhackernews.data.DataConverter;
import com.philosophicalhacker.philhackernews.data.DataFetcher;
import com.philosophicalhacker.philhackernews.data.cache.CachedDataFetcher;
import com.philosophicalhacker.philhackernews.data.cache.HackerNewsCache;
import com.philosophicalhacker.philhackernews.data.cache.HackerNewsContentProvider;
import com.philosophicalhacker.philhackernews.data.cache.HackerNewsDatabaseOpenHelper;
import com.philosophicalhacker.philhackernews.data.remote.HackerNewsRestAdapter;
import com.philosophicalhacker.philhackernews.data.remote.RemoteDataFetcher;
import com.philosophicalhacker.philhackernews.data.sync.HackerNewsSyncService;
import com.philosophicalhacker.philhackernews.model.Item;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 *
 * Provides dependencies for all objects in the data module.
 *
 * Created by MattDupree on 7/19/15.
 */
@Module(library = true,
        injects = {
            HackerNewsContentProvider.class,
            HackerNewsSyncService.class
            },
        complete = false)
public class DataModule {

    public static final String HACKERNEWSDATA_DB_FILE_NAME = "hackernewsdata.db";
    public static final String HACKER_NEWS_API_ENDPOINT = "https://hacker-news.firebaseio.com/v0";

    @Provides
    ContentResolver provideContentResolver(Context context) {
        return context.getContentResolver();
    }

    @Singleton
    @Provides
    HackerNewsCache provideHackerNewsCache(ContentResolver contentResolver) {
        return new HackerNewsCache(contentResolver);
    }

    @Singleton
    @Provides
    HackerNewsRestAdapter privideHackerNewsRestAdapter() {
        RestAdapter build = new RestAdapter.Builder()
                .setEndpoint(HACKER_NEWS_API_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        return build.create(HackerNewsRestAdapter.class);
    }

    @Singleton
    @Provides @Named(RemoteDataFetcher.DAGGER_INJECT_QUALIFIER)
    DataFetcher provideRemoteDataFetcher(HackerNewsRestAdapter hackerNewsRestAdapter) {
        return new RemoteDataFetcher(hackerNewsRestAdapter);
    }

    @Singleton
    @Provides @Named(CachedDataFetcher.DAGGER_INJECT_QUALIFIER)
    DataFetcher provideCachedDataFetcher(ContentResolver contentResolver, DataConverter<List<Item>, Cursor> dataConverter) {
        return new CachedDataFetcher(contentResolver, dataConverter);
    }

    @Singleton
    @Provides
    DataConverter<List<Item>, Cursor> provideCursorToStoryIdsDataConverter() {
        return new CursorToItemConverter();
    }

    @Singleton
    @Provides
    HackerNewsDatabaseOpenHelper provideHackerNewsDatabaseOpenHelper(Context context) {
        return new HackerNewsDatabaseOpenHelper(context, HACKERNEWSDATA_DB_FILE_NAME, null, 1);
    }

    @Singleton
    @Provides
    SQLiteDatabase provideHackerNewsDatabase(HackerNewsDatabaseOpenHelper hackerNewsDatabaseOpenHelper) {
        return hackerNewsDatabaseOpenHelper.getWritableDatabase();
    }
}
