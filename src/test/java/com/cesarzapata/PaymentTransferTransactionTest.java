package com.cesarzapata;

import org.junit.Test;

import static com.cesarzapata.support.Asserts.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;

public class PaymentTransferTransactionTest {

    @Test
    public void constructors() {
        new PaymentTransferTransaction("", "", Money.ZERO);
        new PaymentTransferTransaction(0L, "", "", Money.ZERO);
        assertThrows(NullPointerException.class, () -> new PaymentTransferTransaction(null, "", Money.ZERO));
        assertThrows(NullPointerException.class, () -> new PaymentTransferTransaction("", null, Money.ZERO));
        assertThrows(NullPointerException.class, () -> new PaymentTransferTransaction("", "", null));
    }

    @Test
    public void default_id_is_zero() {
        assertEquals(new PaymentTransferTransaction("", "", Money.ZERO),
                new PaymentTransferTransaction(0L, "", "", Money.ZERO));
    }

    @Test
    public void equality() {
        PaymentTransferTransaction t1 = new PaymentTransferTransaction(1L, "", "", Money.ZERO);

        assertNotSame(t1, new PaymentTransferTransaction(1L, "", "", Money.ZERO));
        assertEquals(t1, new PaymentTransferTransaction(1L, "", "", Money.ZERO));
        assertNotEquals(t1, new PaymentTransferTransaction(2L, "", "", Money.ZERO));
        assertNotEquals(t1, new PaymentTransferTransaction(1L, "1", "", Money.ZERO));
        assertNotEquals(t1, new PaymentTransferTransaction(1L, "", "1", Money.ZERO));
        assertNotEquals(t1, new PaymentTransferTransaction(1L, "", "", new Money("1")));
    }
}