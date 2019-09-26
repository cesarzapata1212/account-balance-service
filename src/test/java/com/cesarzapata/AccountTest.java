package com.cesarzapata;

import org.junit.Test;

import static com.cesarzapata.support.Asserts.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class AccountTest {

    @Test
    public void default_balance() {
        assertEquals(Money.ZERO, new Account("111", "222").balance());
    }

    @Test
    public void new_balance() {
        Account a = new Account("111", "222");
        Account b = a.balance(new Money("100"));
        assertEquals(Money.ZERO, a.balance());
        assertEquals(new Money("100"), b.balance());
    }

    @Test
    public void equality() {
        Account a = new Account("111", "222");
        Account b = new Account("111", "222");
        assertEquals(a, b);
        assertNotSame(a, b);
    }

    @Test
    public void null_safety() {
        assertThrows(NullPointerException.class, () -> new Account("111", "222", null));
        assertThrows(NullPointerException.class, () -> new Account("111", null, Money.ZERO));
        assertThrows(NullPointerException.class, () -> new Account(null, "222", Money.ZERO));
    }
}