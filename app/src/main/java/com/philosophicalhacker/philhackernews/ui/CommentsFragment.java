package com.philosophicalhacker.philhackernews.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.model.Item;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by MattDupree on 7/20/15.
 */
public class CommentsFragment extends Fragment {

    private static final String ARGS_ITEM = "item";

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new CommentsAdapter());
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_story_comments, menu);
    }

    //----------------------------------------------------------------------------------
    // Nested Inner Class
    //----------------------------------------------------------------------------------
    private static class CommentsAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(new TextView(parent.getContext())) {};
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((TextView) holder.itemView).setText("test " + position);
        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }
}
