package com.philosophicalhacker.philhackernews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philosophicalhacker.philhackernews.daggermodules.LoaderModule;
import com.philosophicalhacker.philhackernews.data.StoryRepository;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.ObjectGraph;
import rx.Subscriber;


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        PhilHackerNewsApplication application = (PhilHackerNewsApplication) getActivity().getApplication();
        ObjectGraph plus = application.getObjectGraph().plus(new LoaderModule(this));
        plus.inject(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mStoryRepository.addStoriesSubscriber(new Subscriber<List<Integer>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(final List<Integer> stories) {
                mRecyclerView.setAdapter(new RecyclerView.Adapter() {
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        return new RecyclerView.ViewHolder(new TextView(parent.getContext())) {
                        };
                    }

                    @Override
                    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                        ((TextView) holder.itemView).setText(String.valueOf(stories.get(position)));
                    }

                    @Override
                    public int getItemCount() {
                        return stories.size();
                    }
                });
            }
        });
        mStoryRepository.loadTopStories();
        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
