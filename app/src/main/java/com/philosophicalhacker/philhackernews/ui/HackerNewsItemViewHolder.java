package com.philosophicalhacker.philhackernews.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.philosophicalhacker.philhackernews.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by MattDupree on 7/21/15.
 */
public class HackerNewsItemViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.upvotesTextView)
    public TextView mCircleNumberTextView;

    @Bind(R.id.authorTextView)
    public TextView mAuthorTextView;

    @Bind(R.id.itemTextView)
    public TextView mItemTextView;

    public HackerNewsItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
