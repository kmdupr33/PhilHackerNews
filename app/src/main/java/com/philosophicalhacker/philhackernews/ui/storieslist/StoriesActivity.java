package com.philosophicalhacker.philhackernews.ui.storieslist;

import android.os.Bundle;

import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.ui.refresh.RefreshableFragmentHostingActivity;


public class StoriesActivity extends RefreshableFragmentHostingActivity {

    private static final String CURRENT_FRAG_TAG = "currentFrag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StoriesFragment storiesFragment;
        if (savedInstanceState == null) {
            storiesFragment = new StoriesFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, storiesFragment, CURRENT_FRAG_TAG)
                    .commit();
        } else {
            storiesFragment = (StoriesFragment) getSupportFragmentManager().findFragmentByTag(CURRENT_FRAG_TAG);
        }
        configureRefreshableFragment(storiesFragment);
    }
}
