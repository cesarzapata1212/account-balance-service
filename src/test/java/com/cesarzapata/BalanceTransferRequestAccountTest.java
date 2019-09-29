package com.cesarzapata;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;

public class BalanceTransferRequestAccountTest {

    @Test
    public void equality() {
        BalanceTransferRequest.Account a = new BalanceTransferRequest.Account("0", "0");
        BalanceTransferRequest.Account b = new BalanceTransferRequest.Account("0", "0");
        BalanceTransferRequest.Account c = new BalanceTransferRequest.Account("0", "00");
        assertNotSame(a, b);
        assertEquals(a, b);
        assertNotEquals(a, c);
    }
}