package com.cesarzapata;

public class BalanceTransferResult {

    private final Account destinationAccount;
    private Account sourceAccount;

    public BalanceTransferResult(Account sourceAccount, Account destinationAccount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
    }

    public Account sourceAccount() {
        return this.sourceAccount;
    }

    public Account destinationAccount() {
        return this.destinationAccount;
    }
}
