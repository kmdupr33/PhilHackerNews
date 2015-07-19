package com.philosophicalhacker.philhackernews.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import rx.Observer;
import rx.Subscription;
import rx.observables.ConnectableObservable;

/**
 * Created by MattDupree on 7/19/15.
 */
public class PageTurningOnScrollListener extends RecyclerView.OnScrollListener {

    private final LinearLayoutManager mLayoutManager;
    private final PageLoader mPageLoader;
    private final int mNumItemsRemaining;
    private int mPageNumber = 1;
    private Subscription mSubscription;

    public PageTurningOnScrollListener(LinearLayoutManager layoutManager,
                                       PageLoader loadPageObservable,
                                       int numItemsRemaining) {
        mLayoutManager = layoutManager;
        mPageLoader = loadPageObservable;
        mNumItemsRemaining = numItemsRemaining;
    }

    public void stopPageTurning() {
        mSubscription.unsubscribe();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int itemsRemaining = mLayoutManager.getItemCount() - mLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (itemsRemaining < mNumItemsRemaining) {
            ConnectableObservable<Boolean> pageLoadedObservable = mPageLoader.loadPage(
                    mPageNumber);
            mSubscription = pageLoadedObservable.subscribe(new Observer<Boolean>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    if (aBoolean) {
                        mPageNumber++;
                    }
                }
            });
            pageLoadedObservable.connect();
        }
    }
}
