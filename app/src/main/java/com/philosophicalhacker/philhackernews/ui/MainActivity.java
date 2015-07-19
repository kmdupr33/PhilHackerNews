package com.philosophicalhacker.philhackernews.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.data.content.HackerNewsData;
import com.philosophicalhacker.philhackernews.data.sync.HackerNewsSyncAdapter;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Constants
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "philosophicalhacker.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    public static Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            requestTopStoriesSync();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //----------------------------------------------------------------------------------
    // Helpers
    //----------------------------------------------------------------------------------
    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    private static Account createSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (!accountManager.addAccountExplicitly(newAccount, null, null)) {
            //Call failed. See docs for possible reasons
        }
        return newAccount;
    }

    private void requestTopStoriesSync() {
        mAccount = createSyncAccount(this);
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        settingsBundle.putInt(HackerNewsSyncAdapter.EXTRA_KEY_TOP_STORIES_LIMIT, 20);
        Log.d(TAG, "Requesting data sync for account: " + mAccount);
        ContentResolver.requestSync(mAccount, HackerNewsData.CONTENT_AUTHORITY, settingsBundle);
    }
}
