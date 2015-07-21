package com.philosophicalhacker.philhackernews.ui.storieslist;

import android.os.Bundle;

import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.ui.refresh.RefreshableFragmentHostingActivity;

import butterknife.ButterKnife;


public class StoriesActivity extends RefreshableFragmentHostingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refreshable_fragment_host);
        ButterKnife.bind(this);
        StoriesFragment fragment = new StoriesFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, null)
                .commit();
    }
}
