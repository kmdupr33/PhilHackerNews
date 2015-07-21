package com.philosophicalhacker.philhackernews.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.data.CommentRepository;
import com.philosophicalhacker.philhackernews.data.sync.DataSynchronizer;
import com.philosophicalhacker.philhackernews.model.Item;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by MattDupree on 7/20/15.
 */
public class CommentsFragment extends LoaderFragment {

    private static final String ARGS_ITEM = "item";

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipeToRefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject
    CommentRepository mCommentRepository;

    @Inject
    DataSynchronizer mDataSynchronizer;

    public static CommentsFragment newInstance(Item item) {
        CommentsFragment commentsFragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_ITEM, item);
        commentsFragment.setArguments(args);
        return commentsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_refreshable_list, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        if (savedInstanceState == null) {
            Item item = getArguments().getParcelable(ARGS_ITEM);
            mDataSynchronizer.requestCommentsSync(item, 20);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            RefreshableListSubscriber subscriber = new CommentsRefreshableListSubscriber(mSwipeRefreshLayout, mRecyclerView);
            mSwipeRefreshLayout.setOnRefreshListener(new CommentSyncRequestingRefreshListener(item, mDataSynchronizer));
            mCommentRepository.getCommentsForStoryObservable(item).subscribe(subscriber);
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_story_comments, menu);
    }

    //----------------------------------------------------------------------------------
    // Nested Inner Class
    //----------------------------------------------------------------------------------
    private static class CommentsRefreshableListSubscriber extends RefreshableListSubscriber {

        CommentsRefreshableListSubscriber(SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView) {
            super(swipeRefreshLayout, recyclerView);
        }

        @Override
        protected RecyclerView.Adapter getItemAdapter(List<Item> items) {
            return new CommentsAdapter(items);
        }
    }

    private static class CommentSyncRequestingRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        private DataSynchronizer mDataSynchronizer;
        private Item mItem;

        public CommentSyncRequestingRefreshListener(Item item, DataSynchronizer dataSynchronizer) {
            mItem = item;
            mDataSynchronizer = dataSynchronizer;
        }

        @Override
        public void onRefresh() {
            mDataSynchronizer.requestCommentsSync(mItem, 20);
        }
    }
}
