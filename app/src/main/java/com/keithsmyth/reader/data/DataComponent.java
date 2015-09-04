package com.keithsmyth.reader.data;

import com.keithsmyth.reader.ui.fragment.RssFragment;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = DataModule.class)
public interface DataComponent {
    Bus bus();

    EntryStatusProvider entryStatusProvider();

    void inject(RssFragment fragment);
}
