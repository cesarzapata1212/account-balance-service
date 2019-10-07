package com.cesarzapata.core;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class MoneyTest {

    @Test
    public void equality() {
        assertEquals(new Money("100"), new Money("100"));
        assertEquals(new Money("100.00"), new Money("100"));
        assertNotEquals(new Money("100"), new Money("90"));
        assertNotEquals(new Money("100.01"), new Money("100.00"));
        assertEquals(new Money(BigDecimal.ONE), new Money("1"));
        assertEquals(new Money(new BigDecimal("100.00")), new Money("100"));
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
