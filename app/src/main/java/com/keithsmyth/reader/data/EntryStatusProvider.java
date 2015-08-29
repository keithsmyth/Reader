package com.keithsmyth.reader.data;



import java.util.HashSet;
import java.util.Set;

public final class EntryStatusProvider {

    private static EntryStatusProvider INSTANCE;

    private final Set<String> read;

    private EntryStatusProvider() {
        read = new HashSet<>();
    }

    public static EntryStatusProvider provide() {
        if (INSTANCE == null) {
            INSTANCE = new EntryStatusProvider();
        }
        return INSTANCE;
    }

    public boolean isRead(String entryId) {
        return read.contains(entryId);
    }

    public void setRead(String entryId) {
        read.add(entryId);
    }
}
