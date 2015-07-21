package com.philosophicalhacker.philhackernews.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.data.StoryRepository;
import com.philosophicalhacker.philhackernews.data.sync.DataSynchronizer;
import com.philosophicalhacker.philhackernews.model.Item;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends LoaderFragment {

    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipeToRefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @SuppressWarnings("WeakerAccess")
    @Inject
    StoryRepository mStoryRepository;

    @SuppressWarnings("WeakerAccess")
    @Inject
    DataSynchronizer mDataSynchronizer;

    private Subscription mSubscription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_refreshable_list, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout.setOnRefreshListener(new SyncOnRefreshListener(mDataSynchronizer));
        RefreshableListSubscriber refreshableListSubscriber = new StoriesRefreshableListSubscriber(mSwipeRefreshLayout, mRecyclerView);
        mSubscription = mStoryRepository.addStoriesSubscriber(refreshableListSubscriber);
        mStoryRepository.loadTopStories();
        if (savedInstanceState == null) {
            mDataSynchronizer.requestTopStoriesSync();
        }
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mSubscription.unsubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //----------------------------------------------------------------------------------
    // Nested Inner Class
    //----------------------------------------------------------------------------------
    static class SyncOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        private DataSynchronizer mDataSynchronizer;

        public SyncOnRefreshListener(DataSynchronizer dataSynchronizer) {
            mDataSynchronizer = dataSynchronizer;
        }

        @Override
        public void onRefresh() {
            mDataSynchronizer.requestTopStoriesSync();
        }
    }

    //----------------------------------------------------------------------------------
    // Nested Inner Class
    //----------------------------------------------------------------------------------
    private static class StoriesRefreshableListSubscriber extends RefreshableListSubscriber {
        public StoriesRefreshableListSubscriber(SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView) {
            super(swipeRefreshLayout, recyclerView);
        }

        @Override
        protected RecyclerView.Adapter getItemAdapter(List<Item> items) {
            return new StoriesAdapter(items);
        }
    }
}
