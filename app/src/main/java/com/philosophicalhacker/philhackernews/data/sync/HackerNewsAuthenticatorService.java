package com.philosophicalhacker.philhackernews.data.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by MattDupree on 7/18/15.
 */
public class HackerNewsAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private HackerNewsAuthenticator mAuthenticator;
    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new HackerNewsAuthenticator(this);
    }
    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
