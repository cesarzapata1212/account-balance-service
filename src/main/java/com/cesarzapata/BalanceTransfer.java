package com.cesarzapata;

import org.jetbrains.annotations.NotNull;

public class BalanceTransfer {

    private Accounts accounts;

    public BalanceTransfer(Accounts accounts) {
        this.accounts = accounts;
    }

    public BalanceTransferResult transfer(@NotNull BalanceTransferRequest request) {
        Account sourceAccount = findAccount(request.getSourceAccount());
        Account destinationAccount = findAccount(request.getDestinationAccount());
        Money transferAmount = new Money(request.getAmount());

        BalanceTransferResult result = new BalanceTransferResult(
                sourceAccount.balance(sourceAccount.balance().minus(transferAmount)),
                destinationAccount.balance(destinationAccount.balance().plus(transferAmount))
        );

        accounts.update(result.sourceAccount());
        accounts.update(result.destinationAccount());

        return result;
    }

    private Account findAccount(BalanceTransferRequest.Account account) {
        return accounts.find(account.getAccountNumber(), account.getSortCode());
    }
}
