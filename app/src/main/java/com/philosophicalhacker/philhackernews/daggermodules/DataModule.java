package com.philosophicalhacker.philhackernews.daggermodules;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.CursorLoader;

import com.philosophicalhacker.philhackernews.data.CursorToStoryConverter;
import com.philosophicalhacker.philhackernews.data.DataConverter;
import com.philosophicalhacker.philhackernews.data.DataFetcher;
import com.philosophicalhacker.philhackernews.data.cache.CachedDataFetcher;
import com.philosophicalhacker.philhackernews.data.cache.HackerNewsContentProvider;
import com.philosophicalhacker.philhackernews.data.cache.HackerNewsData;
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
 * Created by MattDupree on 7/19/15.
 */
@Module(library = true,
        injects = {
            HackerNewsContentProvider.class,
            HackerNewsSyncService.class
            },
        complete = false)
public class DataModule {
    @Provides
    ContentResolver provideContentResolver(Context context) {
        return context.getContentResolver();
    }

    @Provides
    HackerNewsRestAdapter privideHackerNewsRestAdapter() {
        RestAdapter build = new RestAdapter.Builder()
                .setEndpoint("https://hacker-news.firebaseio.com/v0")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        return build.create(HackerNewsRestAdapter.class);
    }

    @Provides @Named(RemoteDataFetcher.DAGGER_INJECT_QUALIFIER)
    DataFetcher provideRemoteDataFetcher(HackerNewsRestAdapter hackerNewsRestAdapter) {
        return new RemoteDataFetcher(hackerNewsRestAdapter);
    }

    @Provides @Named(CachedDataFetcher.DAGGER_INJECT_QUALIFIER)
    DataFetcher provideCachedDataFetcher(ContentResolver contentResolver, DataConverter<List<Item>, Cursor> dataConverter) {
        return new CachedDataFetcher(contentResolver, dataConverter);
    }

    @Provides
    CursorLoader provideStoryLoader(Context context) {
        String sortOrder = HackerNewsData.Items.SCORE + " DESC";
        return new CursorLoader(context, HackerNewsData.Items.CONTENT_URI, null, null, null, sortOrder);
    }

    @Provides
    DataConverter<List<Item>, Cursor> provideCursorToStoryIdsLoaderDataConverter() {
        return new CursorToStoryConverter();
    }

    @Singleton
    @Provides
    HackerNewsDatabaseOpenHelper provideHackerNewsDatabaseOpenHelper(Context context) {
        return new HackerNewsDatabaseOpenHelper(context, "hackernewsdata.db", null, 1);
    }

    @Provides
    SQLiteDatabase provideHackerNewsDatabase(HackerNewsDatabaseOpenHelper hackerNewsDatabaseOpenHelper) {
        return hackerNewsDatabaseOpenHelper.getWritableDatabase();
    }
}
