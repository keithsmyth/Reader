package com.keithsmyth.reader.data.controller;

import android.util.Log;

import com.keithsmyth.reader.data.external.GetRssTask;
import com.keithsmyth.reader.data.local.provider.EntryProvider;
import com.keithsmyth.reader.data.local.provider.FeedProvider;
import com.keithsmyth.reader.model.Entry;
import com.keithsmyth.reader.model.Feed;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class RssController {

    private static final String TAG = "RssController";
    private static final long UPDATE_MILLIS = TimeUnit.MINUTES.toMillis(15);

    private final FeedProvider feedProvider;
    private final EntryProvider entryProvider;

    public RssController(FeedProvider feedProvider, EntryProvider entryProvider) {
        this.feedProvider = feedProvider;
        this.entryProvider = entryProvider;
    }

    public Observable<List<Entry>> getRssFeed(Feed feed) {
        // API
        if ((feed.lastFetched.getTime() + UPDATE_MILLIS) < System.currentTimeMillis()) {
            addRssFeed(feed.url);
        }

        // LOCAL DB
        return entryProvider.get(feed.id);
    }

    public void addRssFeed(final String url) {
        new GetRssTask(new GetRssTask.Listener() {
            @Override
            public void onGet(Feed feed) {
                // Save to db
                feedProvider.save(feed);
                entryProvider.save(feed.id, feed.entries);
            }

            @Override
            public void onError(String msg) {
                Log.e(TAG, "onError: " + msg, null);
                // TODO: Bubble up and SnackBar
            }
        }).execute(url);
    }

    public void setEntryRead(String entryId, boolean isRead) {
        entryProvider.setRead(entryId, isRead);
    }
}
