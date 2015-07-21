package com.philosophicalhacker.philhackernews.ui.commentslist;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.data.repository.CommentRepository;
import com.philosophicalhacker.philhackernews.data.sync.DataSynchronizer;
import com.philosophicalhacker.philhackernews.model.Item;
import com.philosophicalhacker.philhackernews.ui.refresh.Refreshable;
import com.philosophicalhacker.philhackernews.ui.refresh.RefreshableListRepositoryFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;

/**
 *
 * Created by MattDupree on 7/20/15.
 */
public class CommentsFragment extends RefreshableListRepositoryFragment implements Refreshable {

    private static final String ARGS_ITEM = "item";

    @Inject
    CommentRepository mCommentRepository;

    @Inject
    DataSynchronizer mDataSynchronizer;
    private Item mStory;

    public static CommentsFragment newInstance(Item item) {
        CommentsFragment commentsFragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_ITEM, item);
        commentsFragment.setArguments(args);
        return commentsFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        mStory = getArguments().getParcelable(ARGS_ITEM);
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState == null) {
            mDataSynchronizer.requestCommentsSync(mStory, 20);
        }
        return view;
    }

    @Override
    protected RecyclerView.Adapter getAdapter(List<Item> items) {
        return new CommentsAdapter(items);
    }

    @Override
    protected ConnectableObservable<List<Item>> makeConnectableRepositoryObservable() {
        return mCommentRepository.getCommentsForStoryObservable(mStory).publish();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_story_comments, menu);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onShouldRefreshObservableCreated(Observable<Void> swipeToRefreshObservable) {
        swipeToRefreshObservable.subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mDataSynchronizer.requestCommentsSync(mStory, 20);
            }
        });
    }
}
