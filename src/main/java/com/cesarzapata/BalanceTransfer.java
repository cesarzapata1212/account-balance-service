package com.cesarzapata;

public class BalanceTransfer {
    private final Account sourceAccount;
    private final Account destinationAccount;
    private final Money value;

    public BalanceTransfer(Account sourceAccount, Account destinationAccount, Money value) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.value = value;
    }

    public BalanceTransferResult transfer() {
        return new BalanceTransferResult(
                new Account(
                        sourceAccount,
                        sourceAccount.balance().minus(value)
                ),
                new Account(
                        destinationAccount,
                        destinationAccount.balance().plus(value)
                )
        );
    }
}
