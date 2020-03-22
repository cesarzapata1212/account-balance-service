package com.cesarzapata.common.parse;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.cesarzapata.common.util.OptionalInt;
import org.junit.Test;

public class OptionalIntTest {

    @Test
    public void orElse() {
        assertThat(new OptionalInt(null).orElse(10), is(10));
        assertThat(new OptionalInt("10").orElse(5), is(10));
        assertThat(new OptionalInt("invalid number").orElse(5), is(5));
    }
}
