package com.philosophicalhacker.philhackernews.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.model.Item;

import java.util.List;

import rx.Subscriber;

/**
 * Created by MattDupree on 7/21/15.
 */
public abstract class RefreshableListSubscriber extends Subscriber<List<Item>> {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    public RefreshableListSubscriber(SwipeRefreshLayout swipeRefreshLayout,
                              RecyclerView recyclerView) {
        mSwipeRefreshLayout = swipeRefreshLayout;
        mRecyclerView = recyclerView;
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        Toast.makeText(mRecyclerView.getContext(), R.string.story_load_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNext(final List<Item> items) {
        if (items.size() == 0) {
            mSwipeRefreshLayout.setRefreshing(true);
        } else {
            mRecyclerView.setAdapter(getItemAdapter(items));
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    protected abstract RecyclerView.Adapter getItemAdapter(List<Item> items);
}
