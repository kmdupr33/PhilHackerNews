package com.philosophicalhacker.philhackernews;

import android.accounts.Account;
import android.accounts.AccountManager;
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

    // Constants
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "philosophicalhacker.com";
    // The account name
    public static final String ACCOUNT_NAME = "dummyaccount";
    public static Account mAccount;

    public static final String OBJECT_GRAPH = "ObjectGraph";

    private ObjectGraph mObjectGraph;

    //----------------------------------------------------------------------------------
    // Lifecycle Methods
    //----------------------------------------------------------------------------------
    @Override
    public void onCreate() {
        super.onCreate();
        mAccount = createSyncAccount(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        ContextWrapper contextWrapper = new ObjectGraphProvidingContextWrapper(base);
        super.attachBaseContext(contextWrapper);
    }

    //----------------------------------------------------------------------------------
    // Public Methods
    //----------------------------------------------------------------------------------
    public final ObjectGraph getObjectGraph() {
        if (mObjectGraph == null) {
            mObjectGraph = makeObjectGraph();
        }
        return mObjectGraph;
    }

    protected ObjectGraph makeObjectGraph() {
        return mObjectGraph = ObjectGraph.create(new PhilHackerNewsAppModule(getApplicationContext()));
    }


    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    private static Account createSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(ACCOUNT_NAME, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        Account[] accountsByType = accountManager.getAccountsByType(ACCOUNT_TYPE);
        if (!dummyAccountAlreadyAdded(accountsByType)) {
            /*
             * Add the account and account type, no password or user data
             */
            if (!accountManager.addAccountExplicitly(newAccount, null, null)) {
                //Call failed. See docs for possible reasons
            }
        }
        return newAccount;
    }

    private static boolean dummyAccountAlreadyAdded(Account[] accountsByType) {
        for (int i = 0; i < accountsByType.length; i++) {
            if (accountsByType[i].name == ACCOUNT_NAME) {
                return true;
            }
        }
        return false;
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
