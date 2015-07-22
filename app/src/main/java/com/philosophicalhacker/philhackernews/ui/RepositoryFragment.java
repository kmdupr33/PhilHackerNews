package com.philosophicalhacker.philhackernews.ui;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;

import com.philosophicalhacker.philhackernews.PhilHackerNewsApplication;
import com.philosophicalhacker.philhackernews.daggermodules.RepositoryModule;

import dagger.ObjectGraph;

/**
 * Fragment that utilizes a dagger LoaderModule
 *
 * Created by MattDupree on 7/20/15.
 */
public abstract class RepositoryFragment extends Fragment {

    @CallSuper
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        PhilHackerNewsApplication application = (PhilHackerNewsApplication) getActivity().getApplication();
        ObjectGraph plus = application.getObjectGraph().plus(new RepositoryModule(getLoaderManager()));
        plus.inject(this);
    }
}
