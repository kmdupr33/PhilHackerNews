package com.philosophicalhacker.philhackernews.daggermodules;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.philosophicalhacker.philhackernews.data.CursorToStoryConverter;
import com.philosophicalhacker.philhackernews.data.DataConverter;
import com.philosophicalhacker.philhackernews.data.DataFetcher;
import com.philosophicalhacker.philhackernews.data.content.CachedDataFetcher;
import com.philosophicalhacker.philhackernews.data.content.HackerNewsContentProvider;
import com.philosophicalhacker.philhackernews.data.content.HackerNewsData;
import com.philosophicalhacker.philhackernews.data.content.HackerNewsDatabaseOpenHelper;
import com.philosophicalhacker.philhackernews.data.remote.HackerNewsRestAdapter;
import com.philosophicalhacker.philhackernews.data.remote.RemoteDataFetcher;
import com.philosophicalhacker.philhackernews.data.sync.HackerNewsSyncService;
import com.philosophicalhacker.philhackernews.model.Story;

import java.util.List;

import javax.inject.Named;

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
    DataFetcher provideCachedDataFetcher(ContentResolver contentResolver, DataConverter<List<Story>, Cursor> dataConverter) {
        return new CachedDataFetcher(contentResolver, dataConverter);
    }

    @Provides
    CursorLoader provideStoryLoader(Context context) {
        String sortOrder = HackerNewsData.Stories.SCORE + " DESC";
        return new CursorLoader(context, HackerNewsData.Stories.CONTENT_URI, null, null, null, sortOrder);
    }

    @Provides
    DataConverter<List<Story>, Cursor> provideCursorToStoryIdsLoaderDataConverter() {
        return new CursorToStoryConverter();
    }

    @Provides
    HackerNewsDatabaseOpenHelper provideHackerNewsDatabaseOpenHelper(Context context) {
        return new HackerNewsDatabaseOpenHelper(context, "hackernewsdata.db", null, 1);
    }
}
