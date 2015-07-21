package com.philosophicalhacker.philhackernews.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.model.Item;

public class StoryDetailActivity extends AppCompatActivity {

    private static final String EXTRA_STORY = "com.philosophicalhacker.philhackernews.EXTRA_STORY";
    private static final String STORY_FRAG_TAG = "story";
    private static final String COMMENT_FRAG_TAG = "comments";
    private StoryDetailActivityFragment mStoryDetailActivityFragment;
    private Fragment mCommentsFragment;
    private Item mItem;

    public static Intent getStartIntent(Context context, Item item) {
        Intent intent = new Intent(context, StoryDetailActivity.class);
        intent.putExtra(EXTRA_STORY, item);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        mItem = getIntent().getParcelableExtra(EXTRA_STORY);
        mStoryDetailActivityFragment = StoryDetailActivityFragment.newInstance(mItem);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mStoryDetailActivityFragment, STORY_FRAG_TAG)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_comments:
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.container, getCommentsFragment(mItem), COMMENT_FRAG_TAG)
                        .commit();
                break;
            case R.id.action_view_story:
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .replace(R.id.container, mStoryDetailActivityFragment, STORY_FRAG_TAG)
                        .commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public Fragment getCommentsFragment(Item item) {
        if (mCommentsFragment == null) {
            mCommentsFragment = CommentsFragment.newInstance(item);
        }
        return mCommentsFragment;
    }
}
