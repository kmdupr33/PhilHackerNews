package com.philosophicalhacker.philhackernews;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;

import com.philosophicalhacker.philhackernews.daggermodules.PhilHackerNewsAppModule;

import dagger.ObjectGraph;

/**
 * Initializes the Dagger object graph for latter use.
 *
 * Created by MattDupree on 7/16/15.
 */
@SuppressWarnings("WeakerAccess")
public class PhilHackerNewsApplication extends Application {

    public static final String OBJECT_GRAPH = "ObjectGraph";
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

    @Override
    protected void attachBaseContext(Context base) {
        ContextWrapper contextWrapper = new ObjectGraphProvidingContextWrapper(base);
        super.attachBaseContext(contextWrapper);
    }

    //----------------------------------------------------------------------------------
    // Nested Inner Class
    //----------------------------------------------------------------------------------
    private class ObjectGraphProvidingContextWrapper extends ContextWrapper {
        public ObjectGraphProvidingContextWrapper(Context base) {
            super(base);
        }

        @Override
        public Object getSystemService(String name) {
            if (OBJECT_GRAPH.equals(name)) {
                return getObjectGraph();
            }
            return super.getSystemService(name);
        }
    }
}
