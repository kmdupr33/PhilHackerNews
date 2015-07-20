package com.philosophicalhacker.philhackernews.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.model.Story;

public class StoryDetailActivity extends AppCompatActivity {

    private static final String EXTRA_STORY = "com.philosophicalhacker.philhackernews.EXTRA_STORY";

    public static Intent getStartIntent(Context context, Story story) {
        Intent intent = new Intent(context, StoryDetailActivity.class);
        intent.putExtra(EXTRA_STORY, story);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        Story story = getIntent().getParcelableExtra(EXTRA_STORY);
        StoryDetailActivityFragment storyDetailActivityFragment = StoryDetailActivityFragment.newInstance(story);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, storyDetailActivityFragment)
                .commit();
    }
}
