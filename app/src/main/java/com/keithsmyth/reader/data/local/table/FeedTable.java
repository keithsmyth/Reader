package com.keithsmyth.reader.data.local.table;

import android.content.ContentValues;
import android.database.Cursor;

import com.keithsmyth.reader.data.DataUtils;
import com.keithsmyth.reader.model.Feed;

public class FeedTable {

    public static final String TABLE = "Feed";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_URL = "url";
    public static final String COL_LAST_FETCHED = "lastFetched";

    public static final String CREATE = "create table " + TABLE + " (" +
            COL_ID + " text primary key," +
            COL_NAME + " text," +
            COL_URL + " text," +
            COL_LAST_FETCHED + " real)";

    public static final String WHERE_KEY = COL_ID + " = ?";

    public static Feed NO_VALUE = new Feed(null, null, null, null);

    public static Feed mapFromCursor(Cursor cursor) {
        return new Feed(cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                DataUtils.parseDate(cursor.getLong(3)));
    }

    public static void mapToContentValues(Feed value, ContentValues outValues) {
        outValues.clear();
        outValues.put(COL_ID, value.id);
        outValues.put(COL_NAME, value.name);
        outValues.put(COL_URL, value.url);
        outValues.put(COL_LAST_FETCHED, value.lastFetched.getTime());
    }
}
