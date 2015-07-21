package com.philosophicalhacker.philhackernews.data.cache;

import android.net.Uri;
import android.provider.BaseColumns;

import com.philosophicalhacker.philhackernews.model.Item;

/**
 * Created by MattDupree on 7/18/15.
 */
public interface HackerNewsData {
    String CONTENT_AUTHORITY = "com.philosophicalhacker.philhackernews.provider";
    Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    interface Items extends BaseColumns {
        String TABLE_NAME = "items";
        String STORIES_PATH = TABLE_NAME;
        Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(STORIES_PATH).build();
        String SCORE = "score";
        String TITLE = "title";
        String AUTHOR = "author";
        String URL = "url";
        String COMMENTS = "comments";
        String TEXT = "text";
        String TYPE = "type";
        String PARENT = "parent";

        class Selection {
            public static final String ITEM_ID = Items._ID + " = ?";
            public static String[] getItemWithIdArgs(long itemId) {
                return new String[]{String.valueOf(itemId)};
            }
            public static String COMMENTS_FOR_STORY = HackerNewsData.Items.TYPE + " = ? AND " + HackerNewsData.Items.PARENT + " = ? ";

            public static String[] getCommentsForStoryArgs(int storyId) {
                return new String[]{Item.TYPE_COMMENT, String.valueOf(storyId)};
            }
        }
    }
}
