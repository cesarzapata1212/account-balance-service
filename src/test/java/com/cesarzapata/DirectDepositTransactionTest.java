package com.cesarzapata;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DirectDepositTransactionTest {

    @Test
    public void type() {
        assertEquals(TransactionType.DIRECT_DEPOSIT, new DirectDepositTransaction("", "", Money.ZERO).type());
    }
}