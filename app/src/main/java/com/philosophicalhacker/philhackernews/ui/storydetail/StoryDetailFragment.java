package com.philosophicalhacker.philhackernews.ui.storydetail;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.model.Item;
import com.philosophicalhacker.philhackernews.ui.refresh.OnRefreshableViewCreatedListener;
import com.philosophicalhacker.philhackernews.ui.refresh.RefreshStatusListener;
import com.philosophicalhacker.philhackernews.ui.refresh.Refreshable;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;

/**
 * Shows a webview that points to a link or the html text within the HackerNews story.
 */
public class StoryDetailFragment extends Fragment implements Refreshable {

    private static final String ARGS_STORY = "ARGS_STORY";
    private RefreshStatusListener mRefreshStatusListener;
    private OnRefreshableViewCreatedListener mOnRefreshableViewCreatedListener;

    public static StoryDetailFragment newInstance(Item item) {
        StoryDetailFragment storyDetailFragment = new StoryDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_STORY, item);
        storyDetailFragment.setArguments(args);
        return storyDetailFragment;
    }

    @Bind(R.id.webView)
    WebView mWebView;

    //----------------------------------------------------------------------------------
    // Lifecycle Methods
    //----------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_story_detail, container, false);
        ButterKnife.bind(this, view);
        mOnRefreshableViewCreatedListener.onRefreshableViewCreated(mWebView);
        Item item = getArguments().getParcelable(ARGS_STORY);
        if (savedInstanceState == null) {
            //noinspection ConstantConditions
            getActivity().setTitle(item.getTitle());
            mWebView.setWebViewClient(new RefreshIndicatorUpdatingWebViewClient(mRefreshStatusListener));
            String url = item.getUrl();
            if (url != null) {
                mWebView.loadUrl(url);
            } else {
                mWebView.loadData(item.getText(), "text/html", "UTF-8");
            }
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_story_detail, menu);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //----------------------------------------------------------------------------------
    // Refreshable - Interface Methods
    //----------------------------------------------------------------------------------
    @Override
    public void setRefreshStatusListener(RefreshStatusListener refreshStatusListener) {
        mRefreshStatusListener = refreshStatusListener;
    }

    @Override
    public void onShouldRefreshObservableCreated(Observable<Void> swipeToRefreshObservable) {
        swipeToRefreshObservable.subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mWebView.reload();
            }
        });
    }

    @Override
    public void setOnRefreshableViewCreatedListener(OnRefreshableViewCreatedListener onRefreshableViewCreatedListener) {
        mOnRefreshableViewCreatedListener = onRefreshableViewCreatedListener;
    }

    //----------------------------------------------------------------------------------
    // Nested Inner Class
    //----------------------------------------------------------------------------------
    private static class RefreshIndicatorUpdatingWebViewClient extends WebViewClient {

        public RefreshIndicatorUpdatingWebViewClient(RefreshStatusListener refreshStatusListener) {
            mRefreshStatusListener = refreshStatusListener;
        }

        private RefreshStatusListener mRefreshStatusListener;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mRefreshStatusListener.onRefreshingStatusChanged(RefreshStatusListener.REFRESHING);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mRefreshStatusListener.onRefreshingStatusChanged(RefreshStatusListener.NOT_REFRESHING);
        }
    }
}
