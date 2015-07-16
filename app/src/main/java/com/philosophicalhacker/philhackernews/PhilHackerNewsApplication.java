package com.philosophicalhacker.philhackernews;

import android.app.Application;

import com.philosophicalhacker.philhackernews.daggermodules.PhilHackerNewsAppModule;

import dagger.ObjectGraph;

/**
 * Initializes the Dagger object graph for latter use.
 *
 * Created by MattDupree on 7/16/15.
 */
@SuppressWarnings("WeakerAccess")
public class PhilHackerNewsApplication extends Application {

    private ObjectGraph mObjectGraph;

    public final ObjectGraph getObjectGraph() {
        if (mObjectGraph == null) {
            mObjectGraph = makeObjectGraph();
        }
        return mObjectGraph;
    }

    protected ObjectGraph makeObjectGraph() {
        return mObjectGraph = ObjectGraph.create(new PhilHackerNewsAppModule(getApplicationContext()));
    }
}
