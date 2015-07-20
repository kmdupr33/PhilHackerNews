package com.philosophicalhacker.philhackernews.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.philosophicalhacker.philhackernews.PhilHackerNewsApplication;
import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.daggermodules.LoaderModule;
import com.philosophicalhacker.philhackernews.data.StoryRepository;
import com.philosophicalhacker.philhackernews.data.sync.DataSynchronizer;
import com.philosophicalhacker.philhackernews.model.Item;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.ObjectGraph;
import rx.Subscriber;
import rx.Subscription;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        injectDependencies(view, getLoaderManager());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout.setOnRefreshListener(new SyncOnRefreshListener(mDataSynchronizer));
        mSubscription = mStoryRepository.addStoriesSubscriber(mStoriesSubscriber);
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
    // Helpers
    //----------------------------------------------------------------------------------
    private void injectDependencies(View view, LoaderManager loaderMangaer) {
        ButterKnife.bind(this, view);
        PhilHackerNewsApplication application = (PhilHackerNewsApplication) getActivity().getApplication();
        ObjectGraph plus = application.getObjectGraph().plus(new LoaderModule(loaderMangaer));
        plus.inject(this);
    }

    Subscriber<List<Item>> mStoriesSubscriber = new Subscriber<List<Item>>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(getActivity(), R.string.story_load_error, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(final List<Item> stories) {
            if (stories.size() == 0) {
                mSwipeRefreshLayout.setRefreshing(true);
            } else {
                mRecyclerView.setAdapter(new StoriesAdapter(stories));
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        }
    };

    //----------------------------------------------------------------------------------
    // Nested Inner Class
    //----------------------------------------------------------------------------------
    private static class SyncOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        private DataSynchronizer mDataSynchronizer;

        public SyncOnRefreshListener(DataSynchronizer dataSynchronizer) {
            mDataSynchronizer = dataSynchronizer;
        }

        @Override
        public void onRefresh() {
            mDataSynchronizer.requestTopStoriesSync();
        }
    }
}
