package com.philosophicalhacker.philhackernews.ui.storieslist;

import android.os.Bundle;

import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.ui.RefreshableFragmentHostingActivity;
import com.philosophicalhacker.philhackernews.ui.storydetail.StoryDetailFragment;


public class StoriesActivity extends RefreshableFragmentHostingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            StoryDetailFragment fragment = new StoryDetailFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment, null)
                    .commit();

        }
    }
}
