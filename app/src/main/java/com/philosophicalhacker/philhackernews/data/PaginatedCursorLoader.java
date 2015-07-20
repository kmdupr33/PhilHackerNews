package com.philosophicalhacker.philhackernews.data;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

/**
 * Created by MattDupree on 7/20/15.
 */
public class PaginatedCursorLoader extends CursorLoader {
    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     */
    public PaginatedCursorLoader(Context context) {
        super(context);
    }

    public PaginatedCursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }
}
