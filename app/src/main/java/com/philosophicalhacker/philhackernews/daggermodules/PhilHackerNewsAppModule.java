package com.philosophicalhacker.philhackernews.daggermodules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Provides top level dependencies for all objects in the app.
 *
 * Created by MattDupree on 7/16/15.
 */
@Module(library = true,
        includes = DataModule.class
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
}
