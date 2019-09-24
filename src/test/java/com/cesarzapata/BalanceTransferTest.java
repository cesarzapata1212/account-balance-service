package com.cesarzapata;

import com.cesarzapata.support.FakeAccountRepository;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class BalanceTransferTest {

    private final Account sourceAccount = new Account("00001111", "000111", new Money("1000"));
    private final Account destinationAccount = new Account("00002222", "000222", new Money(BigDecimal.ZERO));
    private Accounts fakeAccounts;

    @Before
    public void setUp() {
        fakeAccounts = new FakeAccountRepository(Arrays.asList(
                sourceAccount,
                destinationAccount
        ));
    }

    @Test
    public void should_make_balance_transfer() {
        BalanceTransferResult result = new BalanceTransfer(fakeAccounts).transfer(
                new BalanceTransferRequest(
                        new BalanceTransferRequest.Account(sourceAccount.accountNumber(), sourceAccount.sortCode()),
                        new BalanceTransferRequest.Account(destinationAccount.accountNumber(), destinationAccount.sortCode()),
                        "275"
                ));

        assertThat(result.sourceAccount(), equalTo(new Account(sourceAccount.accountNumber(), sourceAccount.sortCode(), new Money("725"))));
        assertThat(result.destinationAccount(), equalTo(new Account(destinationAccount.accountNumber(), destinationAccount.sortCode(), new Money("275"))));
        assertThat(result.sourceAccount(), equalTo(fakeAccounts.find(sourceAccount.accountNumber(), sourceAccount.sortCode())));
        assertThat(result.destinationAccount(), equalTo(fakeAccounts.find(destinationAccount.accountNumber(), destinationAccount.sortCode())));
    }

}
