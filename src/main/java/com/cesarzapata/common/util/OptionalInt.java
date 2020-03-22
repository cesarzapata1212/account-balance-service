package com.cesarzapata.common.util;

public class OptionalInt {
    private final String value;

    public OptionalInt(final String value) {
        this.value = value;
    }

    public Integer orElse(final int other) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return other;
        }
    }
}
