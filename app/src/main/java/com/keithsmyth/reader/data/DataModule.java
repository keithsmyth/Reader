package com.keithsmyth.reader.data;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {

    @Provides @Singleton public Bus provideBus() {
        return new Bus();
    }

    @Provides @Singleton public EntryStatusProvider provideEntryStatusProvider() {
        return new EntryStatusProvider();
    }
}
