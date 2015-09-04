package com.keithsmyth.reader.data.local.provider;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.keithsmyth.reader.data.local.table.EntryTable;
import com.keithsmyth.reader.model.Entry;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;

@Singleton
public final class EntryProvider {

    private static final String TAG = "EntryProvider";

    private final BriteDatabase db;
    private final SQLiteOpenHelper sqLiteOpenHelper;

    public EntryProvider(BriteDatabase db, SQLiteOpenHelper sqLiteOpenHelper) {
        this.db = db;
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    public void setRead(String entryId, boolean isRead) {
        final ContentValues values = new ContentValues(1);
        values.put(EntryTable.COL_IS_READ, isRead);
        db.update(EntryTable.TABLE, values, EntryTable.WHERE_PK, entryId);
    }

    public Observable<List<Entry>> get(String feedId) {
        Log.d(TAG, "get() called with: " + "feedId = [" + feedId + "]");
        return db.createQuery(EntryTable.TABLE, String.format("select * from %s where %s = ?", EntryTable.TABLE, EntryTable.COL_FEED_ID), feedId)
                .map(new Func1<SqlBrite.Query, List<Entry>>() {
                    @Override
                    public List<Entry> call(SqlBrite.Query query) {
                        final List<Entry> items = new ArrayList<>();
                        final Cursor cursor = query.run();
                        try {
                            if (cursor.moveToFirst()) {
                                do {
                                    items.add(EntryTable.mapFromCursor(cursor));
                                } while (cursor.moveToNext());
                            }
                        } finally {
                            cursor.close();
                        }
                        return items;
                    }
                });
    }

    public void save(String feedId, List<Entry> values) {
        Log.d(TAG, "save() called with: " + "feedId = [" + feedId + "], values = [" + values.size() + "]");
        final Set<String> readEntries = getEntryKeysByFeed(feedId);
        final BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            final ContentValues contentValues = new ContentValues();
            for (Entry value : values) {
                EntryTable.mapToContentValues(value, contentValues, false);
                if (readEntries.contains(value.id)) {
                    db.update(EntryTable.TABLE, contentValues, EntryTable.WHERE_PK, value.id);
                } else {
                    db.insert(EntryTable.TABLE, contentValues);
                }
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    private Set<String> getEntryKeysByFeed(String feedId) {
        final Set<String> readEntries = new HashSet<>();
        final SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        final Cursor cursor = sqLiteDatabase.query(EntryTable.TABLE,
                new String[]{EntryTable.COL_ID},
                EntryTable.COL_FEED_ID + " = ?",
                new String[]{feedId},
                null,
                null,
                null);
        try {
            while (cursor.moveToNext()) {
                readEntries.add(cursor.getString(0));
            }
        } finally {
            cursor.close();
        }
        return readEntries;
    }
}
