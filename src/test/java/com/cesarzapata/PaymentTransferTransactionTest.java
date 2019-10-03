package com.cesarzapata;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PaymentTransferTransactionTest {

    @Test
    public void type() {
        assertEquals(TransactionType.PAYMENT_TRANSFER, new PaymentTransferTransaction("", "", Money.ZERO).type());
    }
}