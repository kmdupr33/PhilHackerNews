package com.philosophicalhacker.philhackernews.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.model.Item;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by MattDupree on 7/19/15.
 */
class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoryViewHolder> {
    private final List<Item> mStories;

    public StoriesAdapter(List<Item> stories) {
        mStories = stories;
    }

    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.story_list_item, parent, false);
        return new StoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StoryViewHolder holder, int position) {
        final Item item = mStories.get(position);
        String scoreText;
        int score = item.getScore();
        if (score > 999) {
            scoreText = "999+";
        } else {
            scoreText = String.valueOf(score);
        }
        holder.mUpvotesTextView.setText(scoreText);
        holder.mStoryTitleTextView.setText(item.getTitle());
        holder.mAuthorTextView.setText(item.getAuthor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent startIntent = StoryDetailActivity.getStartIntent(context, item);
                context.startActivity(startIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStories.size();
    }

    static class StoryViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.upvotesTextView)
        TextView mUpvotesTextView;

        @Bind(R.id.storyTitleTextView)
        TextView mStoryTitleTextView;

        @Bind(R.id.authorTextView)
        TextView mAuthorTextView;

        public StoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
