package com.philosophicalhacker.philhackernews.ui.commentslist;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.model.Item;
import com.philosophicalhacker.philhackernews.ui.HackerNewsItemViewHolder;
import com.philosophicalhacker.philhackernews.ui.ItemAdapter;

import java.util.List;

/**
 *
 * Created by MattDupree on 7/21/15.
 */
class CommentsAdapter extends ItemAdapter {

    public CommentsAdapter(List<Item> items) {
        super(items);
    }

    @Override
    protected View getItemView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.comment_list_item, parent, false);
    }

    @Override
    public void onBindViewHolder(HackerNewsItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Item item = mItems.get(position);
        //Comment score is unavailable, so we show the comment's position in the list instead.
        holder.mCircleNumberTextView.setText(String.valueOf(position + 1));
        holder.mItemTextView.setText(Html.fromHtml(item.getText()));
    }
}
