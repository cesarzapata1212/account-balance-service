package com.cesarzapata.support;

import java.util.concurrent.Callable;

import static org.junit.Assert.fail;

public class Asserts {

    public static void assertThrows(Class e, Callable fun) {
        try {
            fun.call();
            fail("Expected expection: " + e.getName() + " but none was thrown");
        } catch (Exception inner) {
            if (!inner.getClass().equals(e)) {
                fail("Expected: " + e.getName() + " but got: " + inner.getClass().getName());
            }
        }
    }
}
