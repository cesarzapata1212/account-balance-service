package com.cesarzapata.common.util;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class OptionalString {
    private final String value;

    public OptionalString(final String value) {
        this.value = value;
    }

    public String orElse(final String other) {
        return isEmpty(value) ? other : value;
    }
}
