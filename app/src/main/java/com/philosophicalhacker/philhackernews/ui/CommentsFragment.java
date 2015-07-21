package com.philosophicalhacker.philhackernews.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.data.CommentRepository;
import com.philosophicalhacker.philhackernews.data.sync.DataSynchronizer;
import com.philosophicalhacker.philhackernews.model.Item;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * Created by MattDupree on 7/20/15.
 */
public class CommentsFragment extends LoaderFragment {

    private static final String ARGS_ITEM = "item";

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Inject
    CommentRepository mCommentRepository;

    @Inject
    DataSynchronizer mDataSynchronizer;

    public static CommentsFragment newInstance(Item item) {
        CommentsFragment commentsFragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_ITEM, item);
        commentsFragment.setArguments(args);
        return commentsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comments, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        if (savedInstanceState == null) {
            Item item = getArguments().getParcelable(ARGS_ITEM);
            mDataSynchronizer.requestCommentsSync(item, 20);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mCommentRepository.getCommentsForStoryObservable(item).subscribe(mCommentsSubscriber);
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_story_comments, menu);
    }

    //----------------------------------------------------------------------------------
    // Helpers
    //----------------------------------------------------------------------------------
    Subscriber<List<Item>> mCommentsSubscriber = new Subscriber<List<Item>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<Item> items) {
            mRecyclerView.setAdapter(new CommentsAdapter(items));
        }
    };

    //----------------------------------------------------------------------------------
    // Nested Inner Class
    //----------------------------------------------------------------------------------
    private static class CommentsAdapter extends RecyclerView.Adapter {
        private List<Item> mItems;

        public CommentsAdapter(List<Item> items) {
            mItems = items;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(new TextView(parent.getContext())) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Item item = mItems.get(position);
            ((TextView) holder.itemView).setText(item.getText());
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }
}
