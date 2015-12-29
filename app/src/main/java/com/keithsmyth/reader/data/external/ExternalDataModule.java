package com.keithsmyth.reader.data.external;

import com.keithsmyth.reader.data.controller.FeedController;
import com.keithsmyth.reader.data.controller.RssController;
import com.keithsmyth.reader.data.local.provider.EntryProvider;
import com.keithsmyth.reader.data.local.provider.FeedProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ExternalDataModule {

    @Provides
    @Singleton
    public RssController provideRssController(FeedProvider feedProvider, EntryProvider provider) {
        return new RssController(feedProvider, provider);
    }

    @Provides
    @Singleton
    public FeedController provideFeedController(FeedProvider feedProvider) {
        return new FeedController(feedProvider);
    }
}
