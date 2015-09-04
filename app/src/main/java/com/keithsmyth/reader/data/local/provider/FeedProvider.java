package com.keithsmyth.reader.data.local.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.keithsmyth.reader.data.local.table.FeedTable;
import com.keithsmyth.reader.model.Feed;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.Date;

import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@Singleton
public class FeedProvider {

    private static final String TAG = "FeedProvider";

    private final BriteDatabase db;

    public FeedProvider(BriteDatabase db) {
        this.db = db;
    }

    public Observable<Feed> get(String feedId) {
        Log.d(TAG, "get() called with: " + "feedId = [" + feedId + "]");
        return db.createQuery(FeedTable.TABLE, "select * from " + FeedTable.TABLE + " where id = ?", feedId)
                .map(new Func1<SqlBrite.Query, Feed>() {
                    @Override
                    public Feed call(SqlBrite.Query query) {
                        final Cursor cursor = query.run();
                        try {
                            if (cursor.moveToFirst()) {
                                return FeedTable.mapFromCursor(cursor);
                            } else {
                                return FeedTable.NO_VALUE;
                            }
                        } finally {
                            cursor.close();
                        }
                    }
                });
    }

    public void save(Feed value) {
        Log.d(TAG, "save() called with: " + "value = [" + value.id + "]");
        final ContentValues contentValues = new ContentValues();
        FeedTable.mapToContentValues(value, contentValues);

        final String feedId = value.id;

        // TODO: no need to get and map entire feed, just check feed exists
        get(feedId)
                .subscribeOn(Schedulers.immediate())
                .observeOn(Schedulers.immediate())
                .subscribe(new Action1<Feed>() {
                    @Override
                    public void call(Feed feed) {
                        if (feed == FeedTable.NO_VALUE) {
                            // insert
                            db.insert(FeedTable.TABLE, contentValues);
                        } else {
                            // update
                            db.update(FeedTable.TABLE, contentValues, FeedTable.WHERE_KEY, feedId);
                        }
                    }
                })
                .unsubscribe();
    }

    public void updateFeedFetched(String feedId, Date dateFetched) {
        final ContentValues contentValues = new ContentValues(1);
        contentValues.put(FeedTable.COL_LAST_FETCHED, dateFetched.getTime());
        db.update(FeedTable.TABLE, contentValues, FeedTable.WHERE_KEY, feedId);
    }
}
