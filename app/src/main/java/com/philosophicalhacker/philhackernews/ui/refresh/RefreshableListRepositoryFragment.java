package com.philosophicalhacker.philhackernews.ui.refresh;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.model.Item;
import com.philosophicalhacker.philhackernews.ui.RepositoryFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;

/**
 *
 * Created by MattDupree on 7/21/15.
 */
public abstract class RefreshableListRepositoryFragment extends RepositoryFragment implements Refreshable {

    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private ConnectableObservable<List<Item>> mConnectableRepositoryObservable;
    private RefreshStatusListener mRefreshStatusListener;
    private OnRefreshableViewCreatedListener mOnRefreshableViewCreatedListener;
    private Subscription mSubscription;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mConnectableRepositoryObservable = makeConnectableRepositoryObservable();
    }

    @Override
    public void setRefreshStatusListener(RefreshStatusListener refreshStatusListener) {
        mRefreshStatusListener = refreshStatusListener;
    }

    @Override
    public void setOnRefreshableViewCreatedListener(OnRefreshableViewCreatedListener onRefreshableViewCreatedListener) {
        mOnRefreshableViewCreatedListener = onRefreshableViewCreatedListener;
    }

    @CallSuper
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_recycler, container, false);
        ButterKnife.bind(this, view);
        mOnRefreshableViewCreatedListener.onRefreshableViewCreated(mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSubscription = mConnectableRepositoryObservable.subscribe(new Action1<List<Item>>() {
            @Override
            public void call(List<Item> items) {
                int refreshStatus = getRefreshStatus(items);
                mRefreshStatusListener.onRefreshingStatusChanged(refreshStatus);
                mRecyclerView.setAdapter(getAdapter(items));
            }
        });
        return view;
    }

    private @RefreshStatusListener.RefreshStatus int getRefreshStatus(List<Item> items) {
        return items.size() > 0 ? RefreshStatusListener.NOT_REFRESHING : RefreshStatusListener.REFRESHING;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mConnectableRepositoryObservable.connect();
        mRefreshStatusListener.onRefreshingStatusChanged(RefreshStatusListener.REFRESHING);
    }

    @Override
    public void onStop() {
        super.onStop();
        mSubscription.unsubscribe();
        mRefreshStatusListener.onRefreshingStatusChanged(RefreshStatusListener.NOT_REFRESHING);
    }

    protected abstract RecyclerView.Adapter getAdapter(List<Item> items);

    protected abstract ConnectableObservable<List<Item>> makeConnectableRepositoryObservable();
}
