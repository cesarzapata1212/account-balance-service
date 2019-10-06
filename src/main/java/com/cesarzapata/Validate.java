package com.cesarzapata;

import org.apache.commons.lang3.StringUtils;

public class Validate {

    public static void notBlank(String s, String msg) {
        if (StringUtils.isBlank(s)) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void notNull(Object o, String msg) {
        if (o == null) {
            throw new IllegalArgumentException(msg);
        }
    }
}
