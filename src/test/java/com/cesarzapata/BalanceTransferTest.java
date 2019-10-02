package com.cesarzapata;

import com.cesarzapata.support.FakeAccountRepository;
import com.cesarzapata.support.FakeTransactionRepository;
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
    private Accounts accounts;
    private BalanceTransfer balanceTransfer;
    private FakeTransactionRepository transactions;

    @Before
    public void setUp() {
        accounts = new FakeAccountRepository(Arrays.asList(
                sourceAccount,
                destinationAccount
        ));
        transactions = new FakeTransactionRepository();
        balanceTransfer = new BalanceTransfer(accounts, transactions);
    }

    @Test
    public void should_make_balance_transfer() {
        BalanceTransferResult result = balanceTransfer.transfer(
                new BalanceTransferRequest(
                        new BalanceTransferRequest.Account(sourceAccount.accountNumber(), sourceAccount.sortCode()),
                        new BalanceTransferRequest.Account(destinationAccount.accountNumber(), destinationAccount.sortCode()),
                        new BigDecimal("275")
                ));

        assertThat(result.sourceAccount(), equalTo(new Account(sourceAccount.accountNumber(), sourceAccount.sortCode(), new Money("725"))));
        assertThat(result.destinationAccount(), equalTo(new Account(destinationAccount.accountNumber(), destinationAccount.sortCode(), new Money("275"))));
        assertThat(result.sourceAccount(), equalTo(accounts.find(sourceAccount.accountNumber(), sourceAccount.sortCode())));
        assertThat(result.destinationAccount(), equalTo(accounts.find(destinationAccount.accountNumber(), destinationAccount.sortCode())));
        Transaction sourceTransaction = transactions.find(sourceAccount.accountNumber(), sourceAccount.sortCode());
        assertThat(sourceTransaction, equalTo(new PaymentTransferTransaction(
                sourceAccount.accountNumber(),
                sourceAccount.sortCode(),
                new Money("275")
        )));
    }

    @Test
    public void should_fail_when_source_account_balance_is_insufficient() {
        balanceTransfer.transfer(
                new BalanceTransferRequest(
                        new BalanceTransferRequest.Account(sourceAccount.accountNumber(), sourceAccount.sortCode()),
                        new BalanceTransferRequest.Account(destinationAccount.accountNumber(), destinationAccount.sortCode()),
                        new BigDecimal("1000")
                ));

        thrown.expect(BusinessOperationException.class);
        thrown.expectMessage("INSUFFICIENT_BALANCE");

        balanceTransfer.transfer(
                new BalanceTransferRequest(
                        new BalanceTransferRequest.Account(sourceAccount.accountNumber(), sourceAccount.sortCode()),
                        new BalanceTransferRequest.Account(destinationAccount.accountNumber(), destinationAccount.sortCode()),
                        new BigDecimal("1")
                ));
    }

    @Test
    public void validate_transfer_to_same_account() {
        thrown.expect(BusinessOperationException.class);
        thrown.expectMessage("TRANSFER_TO_SAME_ACCOUNT");

        balanceTransfer.transfer(
                new BalanceTransferRequest(
                        new BalanceTransferRequest.Account(sourceAccount.accountNumber(), sourceAccount.sortCode()),
                        new BalanceTransferRequest.Account(sourceAccount.accountNumber(), sourceAccount.sortCode()),
                        new BigDecimal("10")
                )
        );
    }

    @Test
    public void validate_zero_amount() {
        thrown.expect(BusinessOperationException.class);
        thrown.expectMessage("INVALID_AMOUNT");

        balanceTransfer.transfer(
                new BalanceTransferRequest(
                        new BalanceTransferRequest.Account(sourceAccount.accountNumber(), sourceAccount.sortCode()),
                        new BalanceTransferRequest.Account(destinationAccount.accountNumber(), destinationAccount.sortCode()),
                        BigDecimal.ZERO
                )
        );
    }

    @Test
    public void validate_negative_amount() {
        thrown.expect(BusinessOperationException.class);
        thrown.expectMessage("INVALID_AMOUNT");

        balanceTransfer.transfer(
                new BalanceTransferRequest(
                        new BalanceTransferRequest.Account(sourceAccount.accountNumber(), sourceAccount.sortCode()),
                        new BalanceTransferRequest.Account(destinationAccount.accountNumber(), destinationAccount.sortCode()),
                        BigDecimal.TEN.negate()
                )
        );
    }
}
