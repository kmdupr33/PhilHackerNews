package com.philosophicalhacker.philhackernews.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.model.Item;

import java.util.List;

/**
 *
 * Created by MattDupree on 7/19/15.
 */
class StoriesAdapter extends ItemAdapter {

    public StoriesAdapter(List<Item> stories) {
        super(stories);
    }

    @Override
    protected View getItemView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.story_list_item, parent, false);
    }

    @Override
    public void onBindViewHolder(HackerNewsItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        final Item item = mItems.get(position);
        holder.mItemTextView.setText(item.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent startIntent = StoryDetailActivity.getStartIntent(context, item);
                context.startActivity(startIntent);
            }
        });
    }
}
