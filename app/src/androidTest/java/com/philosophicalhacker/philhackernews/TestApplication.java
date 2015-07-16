package com.philosophicalhacker.philhackernews;

import android.content.Context;

import com.philosophicalhacker.philhackernews.daggermodules.LoaderModule;
import com.philosophicalhacker.philhackernews.daggermodules.PhilHackerNewsAppModule;

import dagger.ObjectGraph;

/**
 * Created by MattDupree on 7/16/15.
 */
public class TestApplication extends PhilHackerNewsApplication {
    @Override
    protected ObjectGraph makeObjectGraph() {
        return ObjectGraph.create(new PhilHackerNewsAppModule(getApplicationContext()), new TestLoaderModule());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
