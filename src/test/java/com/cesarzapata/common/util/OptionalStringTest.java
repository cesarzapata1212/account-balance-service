package com.cesarzapata.common.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class OptionalStringTest {

    @Test
    public void orElse() {
        assertThat(new OptionalString(null).orElse("other"), is("other"));
        assertThat(new OptionalString("").orElse("other"), is("other"));
        assertThat(new OptionalString("a value").orElse("other"), is("a value"));
        assertThat(new OptionalString(" ").orElse("other"), is(" "));
    }
}
