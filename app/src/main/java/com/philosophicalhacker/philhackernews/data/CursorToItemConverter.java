package com.philosophicalhacker.philhackernews.data;

import android.database.Cursor;
import android.text.TextUtils;

import com.philosophicalhacker.philhackernews.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by MattDupree on 7/18/15.
 */
public class CursorToItemConverter implements DataConverter<List<Item>, Cursor> {

    private static final int ID_COL_POS = 0;
    public static final int TYPE_COL_POS = 1;
    public static final int SCORE_COL_POS = 2;
    public static final int TITLE_COL_POS = 3;
    public static final int AUTHOR_COL_POS = 4;
    private static final int URL_POS = 5;
    private static final int TEXT_POS = 6;
    private static final int COMMENTS_POS = 7;
    private static final int PARENT_POS = 8;
    private static final int DELETED_POS = 9;

    @Override
    public List<Item> convertData(Cursor data) {
        List<Item> itemIds = new ArrayList<>(data.getCount());
        //Ensure cursor is positioned before its first row
        data.moveToPosition(-1);
        while (data.moveToNext()) {
            int id = data.getInt(ID_COL_POS);
            String type = data.getString(TYPE_COL_POS);
            int score = data.getInt(SCORE_COL_POS);
            String title = data.getString(TITLE_COL_POS);
            String author = data.getString(AUTHOR_COL_POS);
            String url = data.getString(URL_POS);
            String text = data.getString(TEXT_POS);
            String commentsString = data.getString(COMMENTS_POS);
            int parent = data.getInt(PARENT_POS);
            String[] commentIdStrings = commentsString.split(",");
            int[] commentIds = new int[commentIdStrings.length];
            for (int i=0, end=commentIdStrings.length; i<end; i++) {
                if (!TextUtils.isEmpty(commentIdStrings[i])) {
                    commentIds[i] = Integer.parseInt(commentIdStrings[i]);
                }
            }
            boolean deleted = data.getInt(DELETED_POS) == 1;
            //noinspection ResourceType - type received from database, so we assume valid value
            Item item = new Item(id, type, score, title, author, url, text, commentIds, parent, deleted);
            itemIds.add(item);
        }
        return itemIds;
    }
}
