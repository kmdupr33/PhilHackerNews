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

    private static final int ID_COL_POS = 0;
    public static final int SCORE_COL_POS = 1;
    public static final int TITLE_COL_POS = 2;
    public static final int AUTHOR_COL_POS = 3;

    @Override
    public List<Story> convertData(Cursor data) {
        List<Story> storyIds = new ArrayList<>(data.getCount());
        //Ensure cursor is positioned before its first row
        data.moveToPosition(-1);
        while (data.moveToNext()) {
            int id = data.getInt(ID_COL_POS);
            int score = data.getInt(SCORE_COL_POS);
            String title = data.getString(TITLE_COL_POS);
            String author = data.getString(AUTHOR_COL_POS);
            storyIds.add(new Story(id, score, title, author));
        }
        return storyIds;
    }
}
