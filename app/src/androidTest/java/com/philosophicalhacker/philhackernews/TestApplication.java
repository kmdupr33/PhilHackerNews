package com.philosophicalhacker.philhackernews;

import com.philosophicalhacker.philhackernews.daggermodules.PhilHackerNewsAppModule;

import dagger.ObjectGraph;

/**
 * Created by MattDupree on 7/16/15.
 */
public class TestApplication extends PhilHackerNewsApplication {
    @Override
    protected ObjectGraph makeObjectGraph() {
        return ObjectGraph.create(new PhilHackerNewsAppModule(getApplicationContext()), new TestsModule());
    }
}
