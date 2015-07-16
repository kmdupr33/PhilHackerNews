package com.philosophicalhacker.philhackernews;

import android.app.Application;

import dagger.ObjectGraph;

/**
 * Created by MattDupree on 7/16/15.
 */
public class PhilHackerNewsApplication extends Application {

    private ObjectGraph mObjectGraph;

    public final ObjectGraph getObjectGraph() {
        if (mObjectGraph == null) {
            mObjectGraph = makeObjectGraph();
        }
        return mObjectGraph;
    }

    protected ObjectGraph makeObjectGraph() {
        return mObjectGraph = ObjectGraph.create(Modules.list(getApplicationContext()));
    }
}
