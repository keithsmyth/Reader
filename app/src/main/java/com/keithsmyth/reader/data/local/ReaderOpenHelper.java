package com.keithsmyth.reader.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.keithsmyth.reader.data.local.table.EntryTable;
import com.keithsmyth.reader.data.local.table.FeedTable;

public class ReaderOpenHelper extends SQLiteOpenHelper {

    private static final String NAME = "reader.db";
    private static final int VERSION = 1;

    public ReaderOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FeedTable.CREATE);
        db.execSQL(EntryTable.CREATE);
        prepop(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // no op
    }

    private void prepop(SQLiteDatabase db) {
        final ContentValues values = new ContentValues(4);
        values.put(FeedTable.COL_ID, "tag:blogger.com,1999:blog-6755709643044947179");
        values.put(FeedTable.COL_NAME, "Android Developers Blog");
        values.put(FeedTable.COL_URL, "http://feeds.feedburner.com/blogspot/hsDu?format=xml");
        values.put(FeedTable.COL_LAST_FETCHED, 0);
        db.insert("Feed", null, values);
    }
}
