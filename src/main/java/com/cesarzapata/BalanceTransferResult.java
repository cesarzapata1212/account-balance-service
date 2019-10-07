package com.cesarzapata;

import com.cesarzapata.core.Account;

public class BalanceTransferResult {

    private Account destinationAccount;
    private Account sourceAccount;

    public BalanceTransferResult() {
    }

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
