package com.philosophicalhacker.philhackernews.data;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.io.FileDescriptor;
import java.io.PrintWriter;

/**
 * Created by MattDupree on 7/20/15.
 */
public class PaginatedDataLoaderManager extends LoaderManager {

    private LoaderManager mLoaderManager;

    public PaginatedDataLoaderManager(LoaderManager loaderManager) {
        mLoaderManager = loaderManager;
    }

    @Override
    public <Cursor> Loader<Cursor> initLoader(int id, Bundle args, LoaderCallbacks<Cursor> callback) {
        if (getLoader(id) != null) {

        } else {
            return mLoaderManager.initLoader()
        }
    }

    @Override
    public <D> Loader<D> restartLoader(int id, Bundle args, LoaderCallbacks<D> callback) {
        return mLoaderManager.restartLoader(id, args, callback);
    }

    @Override
    public void destroyLoader(int id) {
        mLoaderManager.destroyLoader(id);
    }

    @Override
    public <D> Loader<D> getLoader(int id) {
        return mLoaderManager.getLoader(id);
    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        mLoaderManager.dump(prefix, fd, writer, args);
    }
}
