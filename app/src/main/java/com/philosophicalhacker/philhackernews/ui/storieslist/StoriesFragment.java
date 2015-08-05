package com.philosophicalhacker.philhackernews.ui.storieslist;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.data.repository.StoryRepository;
import com.philosophicalhacker.philhackernews.data.sync.DataSynchronizer;
import com.philosophicalhacker.philhackernews.model.Item;
import com.philosophicalhacker.philhackernews.ui.refresh.Refreshable;
import com.philosophicalhacker.philhackernews.ui.refresh.RefreshableListRepositoryFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;


/**
 * A placeholder fragment containing a simple view.
 */
public class StoriesFragment extends RefreshableListRepositoryFragment implements Refreshable {

    @SuppressWarnings("WeakerAccess")
    @Inject
    StoryRepository mStoryRepository;

    @SuppressWarnings("WeakerAccess")
    @Inject
    DataSynchronizer mDataSynchronizer;

    //----------------------------------------------------------------------------------
    // Lifecycle Methods
    //----------------------------------------------------------------------------------
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            mDataSynchronizer.requestTopStoriesSync();
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_story_list, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //----------------------------------------------------------------------------------
    // Refreshable Interface Methods
    //----------------------------------------------------------------------------------
    @Override
    public void onShouldRefreshObservableCreated(Observable<Void> swipeToRefreshObservable) {
        swipeToRefreshObservable.subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mDataSynchronizer.requestTopStoriesSync();
            }
        });
    }

    //----------------------------------------------------------------------------------
    // Protected Methods
    //----------------------------------------------------------------------------------
    @Override
    protected RecyclerView.Adapter getAdapter(List<Item> items) {
        return new StoriesAdapter(items);
    }

    @Override
    protected ConnectableObservable<List<Item>> makeConnectableRepositoryObservable() {
        return mStoryRepository.loadTopStories().publish();
    }
}
