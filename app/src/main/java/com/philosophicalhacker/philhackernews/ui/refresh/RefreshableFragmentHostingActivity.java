package com.philosophicalhacker.philhackernews.ui.refresh;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.philosophicalhacker.philhackernews.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;

/**
 * Hosts a {@link RefreshableListRepositoryFragment}. We need this class to faciliate communication
 * between any hosted Fragments and the SwipeToRefreshLayout within this activity. SwipeToRefreshLayout
 * is buggy when contained in a Fragment and its immediate child is ordinarily the view that is being refreshed.
 * In our case, however, the refreshable view is contained inside a Fragment, so this class helps set up the
 * communication the Activity, which owns the SwipeToRefreshLayout and the Fragment, which owns the refreshable view.
 *
 * Created by MattDupree on 7/21/15.
 */
public class RefreshableFragmentHostingActivity extends AppCompatActivity implements RefreshStatusListener, OnRefreshableViewCreatedListener {

    @Bind(R.id.swipeToRefresh)
    CustomChildSwipeRefreshLayout mSwipeRefreshLayout;

    //----------------------------------------------------------------------------------
    // Lifecycle Methods
    //----------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refreshable_fragment_host);
        ButterKnife.bind(this);
    }

    //----------------------------------------------------------------------------------
    // Protected Methods
    //----------------------------------------------------------------------------------
    protected void configureRefreshableFragment(Refreshable refreshable) {
        refreshable.onShouldRefreshObservableCreated(makeRefreshInitiatedObservable());
        refreshable.setRefreshStatusListener(this);
        refreshable.setOnRefreshableViewCreatedListener(this);
    }

    //----------------------------------------------------------------------------------
    // RefreshStatusListener - Interface Methods
    //----------------------------------------------------------------------------------
    @Override
    public void onRefreshingStatusChanged(@RefreshStatus int status) {
        switch (status) {
            case RefreshStatusListener.REFRESHING:
                mSwipeRefreshLayout.setRefreshing(true);
                break;
            case RefreshStatusListener.NOT_REFRESHING:
                mSwipeRefreshLayout.setRefreshing(false);
                break;
        }
    }

    //----------------------------------------------------------------------------------
    // OnRefreshableViewCreatedListener Interface Methods
    //----------------------------------------------------------------------------------
    @Override
    public void onRefreshableViewCreated(View refreshableView) {
        mSwipeRefreshLayout.setCustomChild(refreshableView);
    }

    //----------------------------------------------------------------------------------
    // Helpers
    //----------------------------------------------------------------------------------
    private Observable<Void> makeRefreshInitiatedObservable() {
        return Observable.create(new RefreshListenerSettingOnSubscribe(mSwipeRefreshLayout));
    }

    //----------------------------------------------------------------------------------
    // Nested Inner Class
    //----------------------------------------------------------------------------------
    private static class RefreshListenerSettingOnSubscribe implements Observable.OnSubscribe<Void> {
        private SwipeRefreshLayout mSwipeRefreshLayout;

        private RefreshListenerSettingOnSubscribe(SwipeRefreshLayout swipeRefreshLayout) {
            mSwipeRefreshLayout = swipeRefreshLayout;
        }

        @Override
        public void call(final Subscriber<? super Void> subscriber) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    subscriber.onNext(null);
                }
            });
        }
    }
}
