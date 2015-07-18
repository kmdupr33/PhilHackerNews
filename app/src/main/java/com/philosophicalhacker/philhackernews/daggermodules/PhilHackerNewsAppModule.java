package com.philosophicalhacker.philhackernews.daggermodules;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.support.v4.content.CursorLoader;

import com.philosophicalhacker.philhackernews.data.CursorToStoryIdsConverter;
import com.philosophicalhacker.philhackernews.data.DataFetcher;
import com.philosophicalhacker.philhackernews.data.content.CachedDataFetcher;
import com.philosophicalhacker.philhackernews.data.remote.HackerNewsRestAdapter;
import com.philosophicalhacker.philhackernews.data.DataConverter;
import com.philosophicalhacker.philhackernews.data.content.HackerNewsContentProvider;
import com.philosophicalhacker.philhackernews.data.content.HackerNewsData;
import com.philosophicalhacker.philhackernews.data.content.HackerNewsDatabaseOpenHelper;
import com.philosophicalhacker.philhackernews.data.remote.RemoteDataFetcher;
import com.philosophicalhacker.philhackernews.data.sync.HackerNewsSyncService;

import java.util.List;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Provides top level dependencies for all objects in the app.
 *
 * Created by MattDupree on 7/16/15.
 */
@Module(library = true,
        injects = {
                HackerNewsContentProvider.class,
                HackerNewsSyncService.class
            }
        )
public class PhilHackerNewsAppModule {
    private Context mApplicationContext;

    public PhilHackerNewsAppModule(Context applicationContext) {
        mApplicationContext = applicationContext;
    }

    @Provides
    Context provideContext() {
        return mApplicationContext;
    }

    @Provides
    ConnectivityManager provideConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    ContentResolver provideContentResolver(Context context) {
        return context.getContentResolver();
    }

    @Provides
    HackerNewsRestAdapter privideHackerNewsDataSource() {
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
    DataFetcher provideCachedDataFetcher(ContentResolver contentResolver, DataConverter<List<Integer>, Cursor> dataConverter) {
        return new CachedDataFetcher(contentResolver, dataConverter);
    }

    @Provides
    CursorLoader provideStoryLoader(Context context) {
        String sortOrder = HackerNewsData.Stories.SCORE + " DESC";
        return new CursorLoader(context, HackerNewsData.Stories.CONTENT_URI, null, null, null, sortOrder);
    }

    @Provides
    DataConverter<List<Integer>, Cursor> provideCursorToStoryIdsLoaderDataConverter() {
        return new CursorToStoryIdsConverter();
    }

    @Provides
    HackerNewsDatabaseOpenHelper provideHackerNewsDatabaseOpenHelper(Context context) {
        return new HackerNewsDatabaseOpenHelper(context, "hackernewsdata.db", null, 1);
    }
}
