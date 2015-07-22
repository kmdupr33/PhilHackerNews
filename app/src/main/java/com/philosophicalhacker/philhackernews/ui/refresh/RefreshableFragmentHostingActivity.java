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
