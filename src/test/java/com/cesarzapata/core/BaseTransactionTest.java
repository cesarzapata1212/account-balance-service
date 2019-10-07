package com.cesarzapata.core;

import org.junit.Test;

import static com.cesarzapata.support.Asserts.assertThrows;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;

public class BaseTransactionTest {

    @Test
    public void constructor_null_safe() {
        assertThrows(NullPointerException.class, () -> new BaseTransactionImpl(null, "", Money.ZERO));
        assertThrows(NullPointerException.class, () -> new BaseTransactionImpl("", null, Money.ZERO));
        assertThrows(NullPointerException.class, () -> new BaseTransactionImpl("", "", null));

        assertThrows(NullPointerException.class, () -> new BaseTransactionImpl(null, "", "", Money.ZERO));
        assertThrows(NullPointerException.class, () -> new BaseTransactionImpl(1L, "", null, Money.ZERO));
        assertThrows(NullPointerException.class, () -> new BaseTransactionImpl(1L, "", "", null));
    }

    @Test
    public void default_id_is_zero() {
        BaseTransactionImpl t = new BaseTransactionImpl("", "", Money.ZERO);
        assertThat(t.id(), equalTo(0L));
    }

    @Test
    public void sets_current_date_by_default() {
        BaseTransactionImpl t = new BaseTransactionImpl("", "", Money.ZERO);
        assertThat(t.dateTime(), notNullValue());
    }

    @Test
    public void equality() {
        BaseTransactionImpl t1 = new BaseTransactionImpl(1L, "", "", Money.ZERO);

        assertNotSame(t1, new BaseTransactionImpl(1L, "", "", Money.ZERO));
        assertEquals(t1, new BaseTransactionImpl(1L, "", "", Money.ZERO));
        assertNotEquals(t1, new BaseTransactionImpl(2L, "", "", Money.ZERO));
        assertNotEquals(t1, new BaseTransactionImpl(1L, "1", "", Money.ZERO));
        assertNotEquals(t1, new BaseTransactionImpl(1L, "", "1", Money.ZERO));
        assertNotEquals(t1, new BaseTransactionImpl(1L, "", "", new Money("1")));
    }

    private class BaseTransactionImpl extends BaseTransaction {

        protected BaseTransactionImpl(Long id, String accountNumber, String sortCode, Money amount) {
            super(id, accountNumber, sortCode, amount);
        }

        protected BaseTransactionImpl(String accountNumber, String sortCode, Money amount) {
            super(accountNumber, sortCode, amount);
        }

        @Override
        public TransactionType type() {
            return null;
        }
    }
}