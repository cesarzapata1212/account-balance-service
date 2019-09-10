package com.cesarzapata;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class MoneyTest {

    @Test
    public void equality() {
        assertTrue(new Money("100").equals(new Money("100")));
        assertFalse(new Money("100").equals(new Money("90")));
    }

    @Test
    public void plus() {
        assertThat(new Money("0").plus(new Money("100")), equalTo(new Money("100")));
    }

    @Test
    public void minus() {
        assertThat(new Money("100").minus(new Money("50")), equalTo(new Money("50")));
    }
}
