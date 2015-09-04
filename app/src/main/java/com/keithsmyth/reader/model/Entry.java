package com.keithsmyth.reader.model;

import java.util.Date;

public class Entry {

    public final String id;
    public final String feedId;
    public final String title;
    public final Date publishDateUtc;
    public final String url;
    public final String thumbnailUrl;
    public final boolean isRead;

    public Entry(String id, String feedId, String title, Date publishDateUtc, String url, String thumbnailUrl, boolean isRead) {
        this.id = id;
        this.feedId = feedId;
        this.title = title;
        this.publishDateUtc = publishDateUtc;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
        this.isRead = isRead;
    }
}
