package com.philosophicalhacker.philhackernews;

import android.app.Application;

import dagger.ObjectGraph;

/**
 * Created by MattDupree on 7/16/15.
 */
public class PhilHackerNewsApplication extends Application {

    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        mObjectGraph = ObjectGraph.create(Modules.list());
    }

    public ObjectGraph getObjectGraph() {
        return mObjectGraph;
    }
}
