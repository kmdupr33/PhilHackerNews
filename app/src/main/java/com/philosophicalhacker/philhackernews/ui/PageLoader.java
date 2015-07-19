package com.philosophicalhacker.philhackernews.ui;

import rx.observables.ConnectableObservable;

/**
 * Created by MattDupree on 7/19/15.
 */
public interface PageLoader {
    ConnectableObservable<Boolean> loadPage(int page);
}
