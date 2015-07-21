package com.philosophicalhacker.philhackernews.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import com.philosophicalhacker.philhackernews.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;

/**
 *
 * Created by MattDupree on 7/21/15.
 */
public class RefreshableFragmentHostingActivity extends AppCompatActivity {

    @Bind(R.id.swipeToRefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refreshable_fragment_host);
        ButterKnife.bind(this);
    }

    protected Observable<Void> makeRefreshInitiatedObservable() {
        return Observable.create(new RefreshListenerSettingOnSubscribe());
    }

    //----------------------------------------------------------------------------------
    // Nested Inner Class
    //----------------------------------------------------------------------------------
    private class RefreshListenerSettingOnSubscribe implements Observable.OnSubscribe<Void> {
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
