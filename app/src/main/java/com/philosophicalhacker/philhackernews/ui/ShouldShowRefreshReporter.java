package com.philosophicalhacker.philhackernews.ui;

import rx.Observable;
import rx.Subscriber;

/**
 *
 * Created by MattDupree on 7/21/15.
 */
public interface ShouldShowRefreshReporter {

    /**
     *
     * @param shouldShowRefreshSubscriber
     */
    void onRegisterShouldShowRefreshSubscriber(Subscriber<Boolean> shouldShowRefreshSubscriber);

    void onSwipeToRefreshObservableCreated(Observable<Void> swipeToRefreshObservable);
}
