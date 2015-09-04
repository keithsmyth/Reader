package com.keithsmyth.reader.data;



import java.util.HashSet;
import java.util.Set;

import javax.inject.Singleton;

@Singleton
public final class EntryStatusProvider {

    private final Set<String> read;

    public EntryStatusProvider() {
        read = new HashSet<>();
    }

    public boolean isRead(String entryId) {
        return read.contains(entryId);
    }

    public void setRead(String entryId) {
        read.add(entryId);
    }
}
