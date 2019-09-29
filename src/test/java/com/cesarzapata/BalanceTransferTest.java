package com.cesarzapata;

import com.cesarzapata.support.FakeAccountRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class BalanceTransferTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private Account sourceAccount = new Account("00001111", "000111", new Money("1000"));
    private Account destinationAccount = new Account("00002222", "000222", new Money(BigDecimal.ZERO));
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

    @Test
    public void should_fail_when_source_account_balance_is_insufficient() {
        new BalanceTransfer(fakeAccounts).transfer(
                new BalanceTransferRequest(
                        new BalanceTransferRequest.Account(sourceAccount.accountNumber(), sourceAccount.sortCode()),
                        new BalanceTransferRequest.Account(destinationAccount.accountNumber(), destinationAccount.sortCode()),
                        "1000"
                ));

        thrown.expect(BusinessOperationException.class);
        thrown.expectMessage("INSUFFICIENT_BALANCE");
        new BalanceTransfer(fakeAccounts).transfer(
                new BalanceTransferRequest(
                        new BalanceTransferRequest.Account(sourceAccount.accountNumber(), sourceAccount.sortCode()),
                        new BalanceTransferRequest.Account(destinationAccount.accountNumber(), destinationAccount.sortCode()),
                        "1"
                ));
    }
}
