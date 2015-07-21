package com.philosophicalhacker.philhackernews.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philosophicalhacker.philhackernews.model.Item;

import java.util.List;

/**
 * Created by MattDupree on 7/21/15.
 */
public abstract class ItemAdapter extends RecyclerView.Adapter<HackerNewsItemViewHolder> {
    protected final List<Item> mItems;

    public ItemAdapter(List<Item> items) {
        mItems = items;
    }

    protected abstract View getItemView(LayoutInflater inflater, ViewGroup parent);

    @Override
    public HackerNewsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new HackerNewsItemViewHolder(getItemView(inflater, parent));
    }

    @Override
    public void onBindViewHolder(HackerNewsItemViewHolder holder, int position) {
        final Item item = mItems.get(position);
        String scoreText;
        int score = item.getScore();
        if (score > 999) {
            scoreText = "999+";
        } else {
            scoreText = String.valueOf(score);
        }
        holder.mCircleNumberTextView.setText(scoreText);
        holder.mAuthorTextView.setText(item.getAuthor());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
