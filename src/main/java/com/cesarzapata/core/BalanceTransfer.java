package com.cesarzapata.core;

import java.math.BigDecimal;

import com.cesarzapata.BalanceTransferRequest;
import com.cesarzapata.BalanceTransferResult;
import org.jetbrains.annotations.NotNull;

public class BalanceTransfer {
    private final Accounts accounts;
    private final Transactions transactions;

    public BalanceTransfer(@NotNull Accounts accounts, @NotNull Transactions transactions) {
        this.accounts = accounts;
        this.transactions = transactions;
    }

    public BalanceTransferResult transfer(@NotNull BalanceTransferRequest req) {
        sourceNotEqualToDestination(req);
        validateAmount(req.getAmount());

        Money amount = new Money(req.getAmount());
        Account sourceAccount = findAccount(req.getSourceAccount());
        enoughBalanceAvailable(sourceAccount, amount);

        Account destinationAccount = findAccount(req.getDestinationAccount());

        BalanceTransferResult result = new BalanceTransferResult(
                sourceAccount.balance(sourceAccount.balance().minus(amount)),
                destinationAccount.balance(destinationAccount.balance().plus(amount))
        );

        accounts.update(result.sourceAccount(), sourceAccount.balance().value());
        accounts.update(result.destinationAccount(), destinationAccount.balance().value());

        transactions.add(new DirectPaymentTransaction(sourceAccount.accountNumber(), sourceAccount.sortCode(), amount));
        transactions.add(new DirectDepositTransaction(destinationAccount.accountNumber(), destinationAccount.sortCode(), amount));

        return result;
    }

    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessOperationException("INVALID_AMOUNT");
        }
    }

    private void sourceNotEqualToDestination(BalanceTransferRequest req) {
        if (req.getSourceAccount().equals(req.getDestinationAccount())) {
            throw new BusinessOperationException("TRANSFER_TO_SAME_ACCOUNT");
        }
    }

    private void enoughBalanceAvailable(Account sourceAccount, Money transferAmount) {
        if (sourceAccount.balance().value().compareTo(transferAmount.value()) < 0) {
            throw new BusinessOperationException("INSUFFICIENT_BALANCE");
        }
    }

    private Account findAccount(BalanceTransferRequest.Account account) {
        return accounts.find(account.getAccountNumber(), account.getSortCode());
    }
}
