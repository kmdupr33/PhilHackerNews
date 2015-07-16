package com.philosophicalhacker.philhackernews.daggermodules;

import android.content.Context;

import com.philosophicalhacker.philhackernews.data.HackerNewsRestAdapter;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Provides top level dependencies for all objects in the app.
 *
 * Created by MattDupree on 7/16/15.
 */
@Module(library = true)
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
    HackerNewsRestAdapter provideHackerNewsRestAdapter() {
        RestAdapter build = new RestAdapter.Builder()
                .setEndpoint("https://hacker-news.firebaseio.com/v0")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        return build.create(HackerNewsRestAdapter.class);
    }
}
