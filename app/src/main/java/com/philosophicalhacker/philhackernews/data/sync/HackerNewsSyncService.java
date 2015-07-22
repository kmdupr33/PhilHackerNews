package com.philosophicalhacker.philhackernews.data.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.philosophicalhacker.philhackernews.PhilHackerNewsApplication;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 *
 * Created by MattDupree on 7/18/15.
 */
public class HackerNewsSyncService extends Service {

    @Inject
    DataSynchronizer mDataSynchronizer;

    private static HackerNewsSyncAdapter sSyncAdapter = null;
    // Object to use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        //noinspection ResourceType
        ObjectGraph objectGraph = (ObjectGraph) getApplicationContext().getSystemService(PhilHackerNewsApplication.OBJECT_GRAPH);
        objectGraph.inject(this);
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new HackerNewsSyncAdapter(this, true, mDataSynchronizer);
            }
        }
    }
    /**
     * Return an object that allows the system to invoke
     * the sync adapter.
     *
     */
    @Override
    public IBinder onBind(Intent intent) {
        /*
         * Get the object that allows external processes
         * to call onPerformSync(). The object is created
         * in the base class code when the SyncAdapter
         * constructors call super()
         */
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
