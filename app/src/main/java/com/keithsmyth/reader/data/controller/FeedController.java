package com.keithsmyth.reader.data.controller;

import android.util.Log;

import com.keithsmyth.reader.data.local.provider.FeedProvider;
import com.keithsmyth.reader.model.Feed;

import java.util.List;

import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class FeedController {

    private static final String TAG = "FeedController";

    private final FeedProvider feedProvider;

    public FeedController(FeedProvider feedProvider) {
        this.feedProvider = feedProvider;
    }

    public Observable<Feed> getFeed(String feedId) {
        // Currently goes directly to local
        // TODO: Update with API layer once server side is written
        return feedProvider.get(feedId);
    }

    public Observable<List<Feed>> getFeeds() {
        Log.d(TAG, "getFeeds() called with: " + "");
        // Currently goes directly to local
        // TODO: Update with API layer once server side is written
        return feedProvider.get();
    }
}
