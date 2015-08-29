package com.keithsmyth.reader.ui.event;

import com.keithsmyth.reader.model.Entry;

public class RssItemClickEvent {
    public final Entry entry;

    public RssItemClickEvent(Entry entry) {
        this.entry = entry;
    }
}
