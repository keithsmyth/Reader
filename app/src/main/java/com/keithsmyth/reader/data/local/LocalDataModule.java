package com.keithsmyth.reader.data.local;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import com.keithsmyth.reader.data.local.provider.EntryProvider;
import com.keithsmyth.reader.data.local.provider.FeedProvider;
import com.squareup.otto.Bus;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocalDataModule {

    @Provides
    @Singleton
    public Bus provideBus() {
        return new Bus();
    }

    @Provides
    @Singleton
    public FeedProvider provideFeedProvider(BriteDatabase db) {
        return new FeedProvider(db);
    }

    @Provides
    @Singleton
    public EntryProvider provideEntryProvider(BriteDatabase db, SQLiteOpenHelper sqLiteOpenHelper) {
        return new EntryProvider(db, sqLiteOpenHelper);
    }

    @Provides
    @Singleton
    public SQLiteOpenHelper provideOpenHelper(Application application) {
        return new ReaderOpenHelper(application);
    }

    @Provides
    @Singleton
    public BriteDatabase provideDb(SQLiteOpenHelper helper) {
        final SqlBrite sqlBrite = SqlBrite.create();
        return sqlBrite.wrapDatabaseHelper(helper);
    }
}
