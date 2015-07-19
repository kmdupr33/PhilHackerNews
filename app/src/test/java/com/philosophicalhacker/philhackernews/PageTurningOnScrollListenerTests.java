package com.philosophicalhacker.philhackernews;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.philosophicalhacker.philhackernews.ui.PageLoader;
import com.philosophicalhacker.philhackernews.ui.PageTurningOnScrollListener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * Created by MattDupree on 7/19/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class PageTurningOnScrollListenerTests {

    public static final int NUM_REMAINING_UNTIL_PAGE_TURN = 10;
    @Mock
    RecyclerView mRecyclerView;

    @Mock
    LinearLayoutManager mLayoutManager;

    @Mock
    PageLoader mPageLoader;

    @Test
    public void initializesNewLoadPage() {
        when(mLayoutManager.findFirstCompletelyVisibleItemPosition()).thenReturn(15);
        when(mLayoutManager.getItemCount()).thenReturn(20);
        when(mPageLoader.loadPage(1)).thenReturn(Observable.<Boolean>empty().publish());
        PageTurningOnScrollListener pageTurningOnScrollListener = new PageTurningOnScrollListener(mLayoutManager,
                                                                                                  mPageLoader,
                                                                                                  NUM_REMAINING_UNTIL_PAGE_TURN);
        pageTurningOnScrollListener.onScrolled(mRecyclerView, 0, 0);
        verify(mPageLoader).loadPage(1);
    }
}
