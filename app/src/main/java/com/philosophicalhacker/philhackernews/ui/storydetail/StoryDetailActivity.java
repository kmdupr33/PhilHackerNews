package com.philosophicalhacker.philhackernews.ui.storydetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.model.Item;
import com.philosophicalhacker.philhackernews.ui.commentslist.CommentsFragment;

public class StoryDetailActivity extends AppCompatActivity {

    private static final String EXTRA_STORY = "com.philosophicalhacker.philhackernews.EXTRA_STORY";
    private static final String STORY_FRAG_TAG = "story";
    private static final String COMMENT_FRAG_TAG = "comments";
    private static final String SAVE_STATE_DETAIL_FRAG_SHOW = "SAVE_STATE_DETAIL_FRAG_SHOW";
    private StoryDetailFragment mStoryDetailFragment;
    private Fragment mCommentsFragment;
    private Item mStoryItem;

    public static Intent getStartIntent(Context context, Item item) {
        Intent intent = new Intent(context, StoryDetailActivity.class);
        intent.putExtra(EXTRA_STORY, item);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        mStoryItem = getIntent().getParcelableExtra(EXTRA_STORY);
        if (shouldShowStoryDetailFragment(savedInstanceState)) {
            showStoryDetailFragment(mStoryItem);
        } else {
            showCommentsFragment(mStoryItem);
        }
    }

    private boolean shouldShowStoryDetailFragment(Bundle savedInstanceState) {
        return savedInstanceState == null ||
                savedInstanceState.getBoolean(SAVE_STATE_DETAIL_FRAG_SHOW);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        boolean storyDetailFragShowing = getSupportFragmentManager().findFragmentByTag(STORY_FRAG_TAG) != null;
        outState.putBoolean(SAVE_STATE_DETAIL_FRAG_SHOW, storyDetailFragShowing);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_comments:
                showCommentsFragment(mStoryItem);
                break;
            case R.id.action_view_story:
                showStoryDetailFragment(mStoryItem);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //----------------------------------------------------------------------------------
    // Helpers
    //----------------------------------------------------------------------------------
    private void showStoryDetailFragment(Item item) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, getStoryDetailFragment(item), STORY_FRAG_TAG)
                .commit();
    }

    private void showCommentsFragment(Item item) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, getCommentsFragment(item), COMMENT_FRAG_TAG)
                .commit();
    }

    private Fragment getCommentsFragment(Item item) {
        if (mCommentsFragment == null) {
            mCommentsFragment = CommentsFragment.newInstance(item);
        }
        return mCommentsFragment;
    }

    private Fragment getStoryDetailFragment(Item item) {
        if (mStoryDetailFragment == null) {
            mStoryDetailFragment = StoryDetailFragment.newInstance(item);
        }
        return mStoryDetailFragment;
    }
}
