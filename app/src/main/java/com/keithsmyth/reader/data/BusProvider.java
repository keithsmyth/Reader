package com.keithsmyth.reader.data;

import com.squareup.otto.Bus;

public final class BusProvider {

    private BusProvider() {
        throw new AssertionError("No instances");
    }

    private static Bus BUS;

    public static Bus provide() {
        if (BUS == null) {
            BUS = new Bus();
        }
        return BUS;
    }
}
