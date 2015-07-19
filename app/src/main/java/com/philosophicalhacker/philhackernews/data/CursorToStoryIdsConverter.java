package com.philosophicalhacker.philhackernews.data;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MattDupree on 7/18/15.
 */
public class CursorToStoryIdsConverter implements DataConverter<List<Integer>, Cursor> {

    @Override
    public List<Integer> convertData(Cursor data) {
        List<Integer> storyIds = new ArrayList<>(data.getCount());
        data.moveToFirst();
        while (data.moveToNext()) {
            int anInt = data.getInt(0);
            storyIds.add(anInt);
        }
        return storyIds;
    }
}
