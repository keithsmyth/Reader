package com.keithsmyth.reader.model;

import java.util.Date;

public class Entry {

    public final String id;
    public final String title;
    public final Date publishDateUtc;
    public final String url;
    public final String thumbnailUrl;


    public Entry(String id, String title, Date publishDateUtc, String url, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.publishDateUtc = publishDateUtc;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override public String toString() {
        return String.format("id=%1$s\ntitle=%2$s\npublishDateUtc=%3$s\nurl=%4$s\nthumbnailUrl=%5$s",
            id,
            title,
            publishDateUtc,
            url,
            thumbnailUrl);
    }
}
