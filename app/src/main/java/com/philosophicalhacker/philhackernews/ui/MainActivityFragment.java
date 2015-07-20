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
import com.philosophicalhacker.philhackernews.model.Story;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.ObjectGraph;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements PageLoader {

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
    private List<Story> mStories;
    private StoriesAdapter mAdapter;

    //----------------------------------------------------------------------------------
    // Lifecycle Methods
    //----------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        injectDependencies(view, getLoaderManager());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout.setOnRefreshListener(new SyncOnRefreshListener(mDataSynchronizer));
        Observable<List<Story>> topStoriesObservable = mStoryRepository.getTopStoriesObservable(1,
                                                                                                20);
        mSubscription = topStoriesObservable.subscribe(
                mStoriesSubscriber);
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

    @Override
    public ConnectableObservable<Boolean> loadPage(int page) {
        ConnectableObservable<List<Story>> topStoriesObservable = mStoryRepository.getTopStoriesObservable(
                page, 20).publish();
        topStoriesObservable.subscribe(mStoriesSubscriber);
        return topStoriesObservable.map(new DidStoriesLoadFunc1()).publish();
    }

    //----------------------------------------------------------------------------------
    // Helpers
    //----------------------------------------------------------------------------------
    private void injectDependencies(View view, LoaderManager loaderManager) {
        ButterKnife.bind(this, view);
        PhilHackerNewsApplication application = (PhilHackerNewsApplication) getActivity().getApplication();
        ObjectGraph plus = application.getObjectGraph().plus(new LoaderModule(loaderManager));
        plus.inject(this);
    }

    Subscriber<List<Story>> mStoriesSubscriber = new Subscriber<List<Story>>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(getActivity(), R.string.story_load_error, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(final List<Story> stories) {
            if (mStories == null) {
                mStories = stories;
                mAdapter = new StoriesAdapter(mStories);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                int insertRangeStart = mStories.size();
                mStories.addAll(stories);
                mAdapter.notifyItemRangeInserted(insertRangeStart, stories.size());
            }
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
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

    //----------------------------------------------------------------------------------
    // Nested Inner Class
    //----------------------------------------------------------------------------------
    private static class DidStoriesLoadFunc1 implements Func1<List<Story>, Boolean> {
        @Override
        public Boolean call(List<Story> stories) {
            return stories.size() > 0;
        }
    }
}
