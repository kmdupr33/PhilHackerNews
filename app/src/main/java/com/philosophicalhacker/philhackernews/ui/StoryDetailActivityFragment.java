package com.philosophicalhacker.philhackernews.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.model.Story;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Shows a webview that points to the link within the HackerNews story.
 */
public class StoryDetailActivityFragment extends Fragment {

    private static final String ARGS_STORY = "ARGS_STORY";

    public static StoryDetailActivityFragment newInstance(Story story) {
        StoryDetailActivityFragment storyDetailActivityFragment = new StoryDetailActivityFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_STORY, story);
        storyDetailActivityFragment.setArguments(args);
        return storyDetailActivityFragment;
    }


    @Bind(R.id.webView)
    WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_story_detail, container, false);
        ButterKnife.bind(this, view);
        Story story = getArguments().getParcelable(ARGS_STORY);
        getActivity().setTitle(story.getTitle());
        mWebView.loadUrl(story.getUrl());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_story_detail, menu);
    }

}
