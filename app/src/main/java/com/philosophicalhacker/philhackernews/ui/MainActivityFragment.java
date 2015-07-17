package com.philosophicalhacker.philhackernews.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.philosophicalhacker.philhackernews.PhilHackerNewsApplication;
import com.philosophicalhacker.philhackernews.R;
import com.philosophicalhacker.philhackernews.daggermodules.LoaderModule;
import com.philosophicalhacker.philhackernews.data.StoryRepository;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.ObjectGraph;
import rx.Subscriber;
import rx.Subscription;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @SuppressWarnings("WeakerAccess")
    @Inject
    StoryRepository mStoryRepository;
    private Subscription mSubscription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        injectDependencies(view, getLoaderManager());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSubscription = mStoryRepository.addStoriesSubscriber(mStoriesSubscriber);
        mStoryRepository.loadTopStories();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mSubscription.unsubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //----------------------------------------------------------------------------------
    // Helpers
    //----------------------------------------------------------------------------------
    private void injectDependencies(View view, LoaderManager loaderMangaer) {
        ButterKnife.bind(this, view);
        PhilHackerNewsApplication application = (PhilHackerNewsApplication) getActivity().getApplication();
        ObjectGraph plus = application.getObjectGraph().plus(new LoaderModule(loaderMangaer));
        plus.inject(this);
    }

    Subscriber<List<Integer>> mStoriesSubscriber = new Subscriber<List<Integer>>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(getActivity(), R.string.story_load_error, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(final List<Integer> stories) {
            mRecyclerView.setAdapter(new StoriesAdapter(stories));
        }
    };

    //----------------------------------------------------------------------------------
    // Nested Inner Class
    //----------------------------------------------------------------------------------
    static class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoryViewHolder> {
        private final List<Integer> mStories;

        public StoriesAdapter(List<Integer> stories) {
            mStories = stories;
        }

        @Override
        public StoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.story_list_item, parent, false);
            return new StoryViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(StoryViewHolder holder, int position) {
            holder.mTextView.setText(String.valueOf(mStories.get(position)));
        }

        @Override
        public int getItemCount() {
            return mStories.size();
        }

        static class StoryViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.upvotesTextView)
            TextView mTextView;

            public StoryViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
