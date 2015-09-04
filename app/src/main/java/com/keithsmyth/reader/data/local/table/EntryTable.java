package com.keithsmyth.reader.data.local.table;


import android.content.ContentValues;
import android.database.Cursor;

import com.keithsmyth.reader.data.DataUtils;
import com.keithsmyth.reader.model.Entry;

public class EntryTable {

    public static final String TABLE = "Entry";
    public static final String COL_ID = "id";
    public static final String COL_FEED_ID = "feedId";
    public static final String COL_TITLE = "title";
    public static final String COL_PUBLISH_DATE_UTC = "publishDateUtc";
    public static final String COL_URL = "url";
    public static final String COL_THUMBNAIL_URL = "thumbnailUrl";
    public static final String COL_IS_READ = "isRead";

    public static final String WHERE_PK = COL_ID + " = ?";

    public static final String CREATE = "create table " + TABLE + " (" +
            COL_ID + " text primary key," +
            COL_FEED_ID + " text," +
            COL_TITLE + " text," +
            COL_PUBLISH_DATE_UTC + " real," +
            COL_URL + " text," +
            COL_THUMBNAIL_URL + " text," +
            COL_IS_READ + " number)";

    public static Entry mapFromCursor(Cursor cursor) {
        return new Entry(cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                DataUtils.parseDate(cursor.getLong(3)),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getInt(6) != 0);
    }

    public static void mapToContentValues(Entry value, ContentValues outValues, boolean includeIsRead) {
        outValues.clear();
        outValues.put(COL_ID, value.id);
        outValues.put(COL_FEED_ID, value.feedId);
        outValues.put(COL_TITLE, value.title);
        outValues.put(COL_PUBLISH_DATE_UTC, value.publishDateUtc.getTime());
        outValues.put(COL_URL, value.url);
        outValues.put(COL_THUMBNAIL_URL, value.thumbnailUrl);
        if (includeIsRead) {
            outValues.put(COL_IS_READ, value.isRead);
        }
    }
}
