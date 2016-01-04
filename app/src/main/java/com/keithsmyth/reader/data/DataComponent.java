package com.keithsmyth.reader.data;

import android.database.sqlite.SQLiteOpenHelper;

import com.keithsmyth.reader.AppModule;
import com.keithsmyth.reader.data.controller.FeedController;
import com.keithsmyth.reader.data.controller.RssController;
import com.keithsmyth.reader.data.external.ExternalDataModule;
import com.keithsmyth.reader.data.local.LocalDataModule;
import com.keithsmyth.reader.data.local.provider.EntryProvider;
import com.keithsmyth.reader.data.local.provider.FeedProvider;
import com.keithsmyth.reader.ui.fragment.ListFeedsFragment;
import com.keithsmyth.reader.ui.fragment.RssFragment;
import com.squareup.otto.Bus;
import com.squareup.sqlbrite.BriteDatabase;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ExternalDataModule.class,
        LocalDataModule.class,
        AppModule.class
})
public interface DataComponent {
    Bus bus();

    FeedProvider feedProvider();

    EntryProvider entryProvider();

    SQLiteOpenHelper openHelper();

    BriteDatabase db();

    RssController rssController();

    FeedController feedController();

    void inject(RssFragment fragment);

    void inject(ListFeedsFragment fragment);
}
