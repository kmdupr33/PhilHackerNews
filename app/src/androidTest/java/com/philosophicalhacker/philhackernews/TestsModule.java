package com.philosophicalhacker.philhackernews;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.philosophicalhacker.philhackernews.data.cache.HackerNewsDatabaseOpenHelper;
import com.philosophicalhacker.philhackernews.data.remote.HackerNewsRestAdapter;

import java.io.File;

import dagger.Module;
import dagger.Provides;

/**
 * Overrides the HackerNewsRestAdapter with an adapter that loads a JSON string from memory.
 *
 * Created by MattDupree on 7/16/15.
 */
@Module(overrides = true,
        library = true,
        complete = false,
        injects = {MainActivityTests.class,
                CachingTests.class}
        )
public class TestsModule {
    @Provides
    HackerNewsRestAdapter provideHackerNewsRestAdapter() {
        final Gson gson = new Gson();
        return new MockHackerNewsRestAdapter(gson);
    }
    @Provides
    HackerNewsDatabaseOpenHelper provideHackerNewsDatabaseOpenHelper(Context context) {
        return new HackerNewsDatabaseOpenHelper(context, "test_hackernewsdata.db", null, 1);
    }

    @Provides
    File provideHackerNewsDatabaseFile(SQLiteDatabase sqLiteDatabase) {
        return new File(sqLiteDatabase.getPath());
    }

}
