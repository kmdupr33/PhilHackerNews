package com.philosophicalhacker.philhackernews.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;

import com.philosophicalhacker.philhackernews.data.cache.HackerNewsData;
import com.philosophicalhacker.philhackernews.model.Item;

import java.util.List;

import rx.Observable;

/**
 *
 * Created by MattDupree on 7/20/15.
 */
public class CacheOnlyCommentRepository implements CommentRepository {

    private static final int COMMENT_LOADER = 1;

    private LoaderManager mLoaderManager;
    private Context mContext;
    private DataConverter<List<Item>, Cursor> mDataConverter;

    public CacheOnlyCommentRepository(LoaderManager loaderManager,
                                      Context context,
                                      DataConverter<List<Item>, Cursor> dataConverter) {
        mLoaderManager = loaderManager;
        mContext = context;
        mDataConverter = dataConverter;
    }

    @Override
    public Observable<List<Item>> getCommentsForStoryObservable(Item item) {
        CursorLoader cursorLoader = new CursorLoader(mContext, HackerNewsData.Items.CONTENT_URI, null,
                HackerNewsData.Items.Selection.COMMENTS_FOR_STORY,
                HackerNewsData.Items.Selection.getCommentsForStoryArgs(item.getId()),
                SCORE_DESC_SORT_ORDER);
        return Observable.create(new LoaderInitializingOnSubscribe<>(COMMENT_LOADER, mLoaderManager, cursorLoader, mDataConverter));
    }
}
