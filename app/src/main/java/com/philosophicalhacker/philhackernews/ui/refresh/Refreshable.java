package com.philosophicalhacker.philhackernews.ui.refresh;

import rx.Observable;

/**
 * SwipeToRefreshLayouts must be the top level view in an Activity, so we need this interface to manage
 * communication between the SwipeToRefreshLayout and its child Fragments.
 *
 * Created by MattDupree on 7/21/15.
 */
public interface Refreshable {

    void onRefreshStatusListener(RefreshStatusListener refreshStatusListener);

    void onShouldRefreshObservableCreated(Observable<Void> swipeToRefreshObservable);

    void setOnRefreshableViewCreatedListener(OnRefreshableViewCreatedListener onRefreshableViewCreatedListener);
}
