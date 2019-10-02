package com.cesarzapata.support;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class CompareTo extends BaseMatcher<Comparable> {

    private Comparable expectedValue;

    private CompareTo(Comparable expectedValue) {
        this.expectedValue = expectedValue;
    }

    public static CompareTo compareTo(Comparable expectedValue) {
        return new CompareTo(expectedValue);
    }

    @Override
    public boolean matches(Object actualValue) {
        if (actualValue == null) {
            return expectedValue == null;
        }
        if (!(actualValue instanceof Comparable)) {
            return false;
        }
        return expectedValue.compareTo(actualValue) == 0;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expectedValue);
    }
}
