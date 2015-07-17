package com.philosophicalhacker.philhackernews.daggermodules;

import android.content.Context;
import android.net.ConnectivityManager;

import com.philosophicalhacker.philhackernews.data.HackerNewsCache;
import com.philosophicalhacker.philhackernews.data.HackerNewsDataSource;
import com.philosophicalhacker.philhackernews.data.StoryLoader;

import javax.inject.Named;

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
    ConnectivityManager provideConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides @Named("remote")
    HackerNewsDataSource privideHackerNewsDataSource() {
        RestAdapter build = new RestAdapter.Builder()
                .setEndpoint("https://hacker-news.firebaseio.com/v0")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        return build.create(HackerNewsDataSource.class);
    }
    
    @Provides @Named("cached")
    HackerNewsDataSource provideHackerNewsDataSource() {
        return new HackerNewsCache();
    }

    @Provides @Named("remote")
    StoryLoader provideStoryLoader(Context context, @Named("remote") HackerNewsDataSource hackerNewsDataSource) {
        return new StoryLoader(context, hackerNewsDataSource);
    }
}
