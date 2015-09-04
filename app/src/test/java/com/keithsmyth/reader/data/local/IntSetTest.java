package com.keithsmyth.reader.data.local;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IntSetTest {

    @Test
    public void contains_valueExistsInOddArray_ReturnsTrue() {
        // arrange
        final int[] values = {0, 2, 4, 6, 8};
        final IntSet set = new IntSet(values);

        for (int i : values) {
            // act
            final boolean actual = set.contains(i);

            // assert
            assertTrue("could not find " + i, actual);
        }
    }

    @Test
    public void contains_valueLargerInOddArray_ReturnsFalse() {
        // arrange
        final int[] values = {0, 2, 4, 6, 8};
        final IntSet set = new IntSet(values);

        // act
        final boolean actual = set.contains(10);

        // assert
        assertFalse(actual);
    }

    @Test
    public void contains_valueSmallerInOddArray_ReturnsFalse() {
        // arrange
        final int[] values = {0, 2, 4, 6, 8};
        final IntSet set = new IntSet(values);

        // act
        final boolean actual = set.contains(-10);

        // assert
        assertFalse(actual);
    }

    @Test
    public void contains_valueInbetweenInOddArray_ReturnsFalse() {
        // arrange
        final int[] values = {0, 2, 4, 6, 8};
        final IntSet set = new IntSet(values);

        for (int i : values) {
            final int j = i + 1;
            // act
            final boolean actual = set.contains(j);

            // assert
            assertFalse("could not find " + j, actual);
        }
    }

    @Test
    public void contains_valueExistsInEvenArray_ReturnsTrue() {
        // arrange
        final int[] values = {0, 2, 4, 6};
        final IntSet set = new IntSet(values);

        for (int i : values) {
            // act
            final boolean actual = set.contains(i);

            // assert
            assertTrue("could not find " + i, actual);
        }
    }

    @Test
    public void contains_valueInBetweenInEvenArray_ReturnsFalse() {
        // arrange
        final int[] values = {0, 2, 4, 6};
        final IntSet set = new IntSet(values);

        for (int i : values) {
            final int j = i + 1;
            // act
            final boolean actual = set.contains(j);

            // assert
            assertFalse("could not find " + j, actual);
        }
    }

    @Test
    public void contains_emptyArray_ReturnsFalse() {
        // arrange
        final int[] values = {};
        final IntSet set = new IntSet(values);

        // act
        final boolean actual = set.contains(1);

        // assert
        assertFalse(actual);
    }
}
