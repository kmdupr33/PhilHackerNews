package com.philosophicalhacker.philhackernews.data.content;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by MattDupree on 7/18/15.
 */
public interface HackerNewsData {
    String CONTENT_AUTHORITY = "com.philosophicalhacker.philhackernews.provider";
    Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    interface Stories extends BaseColumns {
        String TABLE_NAME = "stories";
        String STORIES_PATH = TABLE_NAME;
        Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(STORIES_PATH).build();
        String SCORE = "score";
    }
}
