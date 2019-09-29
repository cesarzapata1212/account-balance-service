package com.cesarzapata;

import org.jetbrains.annotations.NotNull;

public class BalanceTransfer {

    private final Accounts accounts;

    public BalanceTransfer(Accounts accounts) {
        this.accounts = accounts;
    }

    public BalanceTransferResult transfer(@NotNull BalanceTransferRequest request) {
        Money transferAmount = new Money(request.getAmount());
        Account sourceAccount = findAccount(request.getSourceAccount());
        validateAvailableBalance(sourceAccount, transferAmount);

        Account destinationAccount = findAccount(request.getDestinationAccount());

        BalanceTransferResult result = new BalanceTransferResult(
                sourceAccount.balance(sourceAccount.balance().minus(transferAmount)),
                destinationAccount.balance(destinationAccount.balance().plus(transferAmount))
        );

        accounts.update(result.sourceAccount());
        accounts.update(result.destinationAccount());

        return result;
    }

    private void validateAvailableBalance(Account sourceAccount, Money transferAmount) {
        if (sourceAccount.balance().value().compareTo(transferAmount.value()) < 0) {
            throw new BusinessOperationException("INSUFFICIENT_BALANCE");
        }
    }

    private Account findAccount(BalanceTransferRequest.Account account) {
        return accounts.find(account.getAccountNumber(), account.getSortCode());
    }
}
