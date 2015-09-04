package com.keithsmyth.reader.data.local;

/**
 * Immutable Set of un-boxed integers
 * Provides a binary search with some small enhancements for efficiency
 * Requires an ordered list of integers
 */
public class IntSet {

    private final int[] values;

    public IntSet(int[] orderedValues) {
        values = orderedValues;
    }

    public boolean contains(int value) {
        final int lastIndex = values.length - 1;
        if (values.length == 0 || value < values[0] || value > values[lastIndex]) {
            return false;
        }

        int from = 0;
        int to = lastIndex;
        while (from <= to) {
            final int mid = (from + to) / 2;
            final int midValue = values[mid];
            if (midValue > value) {
                to = mid - 1;
            } else if (midValue < value) {
                from  = mid + 1;
            } else {
                // found the value
                return true;
            }
        }
        return false;
    }
}
