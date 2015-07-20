package com.philosophicalhacker.philhackernews.ui;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.philosophicalhacker.philhackernews.PhilHackerNewsApplication;
import com.philosophicalhacker.philhackernews.daggermodules.LoaderModule;

import dagger.ObjectGraph;

/**
 * Fragment that utilizes a dagger LoaderModule
 *
 * Created by MattDupree on 7/20/15.
 */
public class LoaderFragment extends Fragment {

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhilHackerNewsApplication application = (PhilHackerNewsApplication) getActivity().getApplication();
        ObjectGraph plus = application.getObjectGraph().plus(new LoaderModule(getLoaderManager()));
        plus.inject(this);
    }
}
