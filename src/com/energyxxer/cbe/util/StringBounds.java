package com.energyxxer.cbe.util;

/**
 * Created by User on 1/1/2017.
 */
public class StringBounds {
    public StringLocation start;
    public StringLocation end;

    public StringBounds(StringLocation start, StringLocation end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "[" +
                "start=" + start +
                " - end=" + end +
                ']';
    }
}