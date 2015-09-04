package com.keithsmyth.reader.data;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataUtils {

    private static final String TAG = "DataUtils";

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS", Locale.getDefault());

    private DataUtils() {
        // no instances
    }

    public static Date parseDate(String value) {
        try {
            return dateFormat.parse(value);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public static Date parseDate(long value) {
        return new Date(value);
    }
}
