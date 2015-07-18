package com.philosophicalhacker.philhackernews.daggermodules;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.support.v4.content.CursorLoader;

import com.philosophicalhacker.philhackernews.data.CursorToStoryIdsLoaderDataConverter;
import com.philosophicalhacker.philhackernews.data.HackerNewsDataSource;
import com.philosophicalhacker.philhackernews.data.LoaderDataConverter;
import com.philosophicalhacker.philhackernews.data.content.HackerNewsContentProvider;
import com.philosophicalhacker.philhackernews.data.content.HackerNewsData;
import com.philosophicalhacker.philhackernews.data.content.HackerNewsDatabaseOpenHelper;
import com.philosophicalhacker.philhackernews.data.sync.HackerNewsSyncService;

import java.util.List;

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
    HackerNewsDataSource privideHackerNewsDataSource() {
        RestAdapter build = new RestAdapter.Builder()
                .setEndpoint("https://hacker-news.firebaseio.com/v0")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        return build.create(HackerNewsDataSource.class);
    }

    @Provides
    CursorLoader provideStoryLoader(Context context) {
        String sortOrder = HackerNewsData.Stories.SCORE + " DESC";
        return new CursorLoader(context, HackerNewsData.Stories.CONTENT_URI, null, null, null, sortOrder);
    }

    @Provides
    LoaderDataConverter<List<Integer>, Cursor> provideCursorToStoryIdsLoaderDataConverter() {
        return new CursorToStoryIdsLoaderDataConverter();
    }

    @Provides
    HackerNewsDatabaseOpenHelper provideHackerNewsDatabaseOpenHelper(Context context) {
        return new HackerNewsDatabaseOpenHelper(context, "hackernewsdata.db", null, 1);
    }
}
