package com.philosophicalhacker.philhackernews.data;

import android.database.Cursor;

import com.philosophicalhacker.philhackernews.data.content.HackerNewsData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MattDupree on 7/18/15.
 */
public class CursorToStoryIdsLoaderDataConverter implements LoaderDataConverter<List<Integer>, Cursor> {

    @Override
    public List<Integer> convertLoaderData(Cursor data) {
        List<Integer> storyIds = new ArrayList<>(data.getCount());
        while (data.moveToNext()) {
            int anInt = data.getInt(0);
            storyIds.add(anInt);
        }
        return storyIds;
    }
}
