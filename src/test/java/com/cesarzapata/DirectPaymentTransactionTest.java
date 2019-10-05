package com.cesarzapata;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DirectPaymentTransactionTest {

    @Test
    public void type() {
        assertEquals(TransactionType.DIRECT_PAYMENT, new DirectPaymentTransaction("", "", Money.ZERO).type());
    }
}