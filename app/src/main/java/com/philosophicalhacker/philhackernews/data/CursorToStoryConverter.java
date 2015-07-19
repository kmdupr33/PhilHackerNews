package com.philosophicalhacker.philhackernews.data;

import android.database.Cursor;

import com.philosophicalhacker.philhackernews.model.Story;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by MattDupree on 7/18/15.
 */
public class CursorToStoryConverter implements DataConverter<List<Story>, Cursor> {

    @Override
    public List<Story> convertData(Cursor data) {
        List<Story> storyIds = new ArrayList<>(data.getCount());
        //Ensure cursor is positioned before its first row
        data.moveToPosition(-1);
        while (data.moveToNext()) {
            int id = data.getInt(0);
            int score = data.getInt(1);
            String title = data.getString(2);
            String author = data.getString(3);
            storyIds.add(new Story(id, score, title, author));
        }
        return storyIds;
    }
}
