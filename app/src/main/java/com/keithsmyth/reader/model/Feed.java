package com.keithsmyth.reader.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Feed {

    public final String id;
    public final String name;
    public final String url;
    public final Date lastFetched;

    public final List<Entry> entries;

    public Feed(String id, String name, String url, Date lastFetched) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.lastFetched = lastFetched;

        entries = new ArrayList<>();
    }
}
