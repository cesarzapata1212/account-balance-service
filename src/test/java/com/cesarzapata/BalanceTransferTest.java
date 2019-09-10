package com.cesarzapata;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class BalanceTransferTest {

    @Test
    public void balanceTransfer() {
        BalanceTransferResult result = new BalanceTransfer(
                new Account("111111", "11110000", new Money("1000")),
                new Account("222222", "22220000", new Money("0")),
                new Money("100")
        ).transfer();

        assertThat(result.sourceAccount().balance(), equalTo(new Money("900")));
        assertThat(result.destinationAccount().balance(), equalTo(new Money("100")));
    }

    @Test
    public void balanceTransfer2() {
        BalanceTransferResult result = new BalanceTransfer(
                new Account("111111", "11111111", new Money("500")),
                new Account("222222", "22222222", new Money("500")),
                new Money("500")
        ).transfer();

        assertThat(result.sourceAccount().balance(), equalTo(new Money("0")));
        assertThat(result.destinationAccount().balance(), equalTo(new Money("1000")));
    }
}
