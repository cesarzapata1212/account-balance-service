package com.cesarzapata;

public class BalanceTransfer {
    private final Account sourceAccount;
    private final Account destinationAccount;
    private final Money amount;

    public BalanceTransfer(Account sourceAccount, Account destinationAccount, Money amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
    }

    public BalanceTransferResult transfer() {
        return new BalanceTransferResult(
                new Account(
                        sourceAccount,
                        sourceAccount.balance().minus(amount)
                ),
                new Account(
                        destinationAccount,
                        destinationAccount.balance().plus(amount)
                )
        );
    }
}
