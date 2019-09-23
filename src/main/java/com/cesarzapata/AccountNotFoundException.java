package com.cesarzapata;

import java.util.function.Supplier;

public class AccountNotFoundException extends RuntimeException implements Supplier<AccountNotFoundException> {
    private final String accountNumber;
    private final String sortCode;

    public AccountNotFoundException(String accountNumber, String sortCode) {
        this.accountNumber = accountNumber;
        this.sortCode = sortCode;
    }

    @Override
    public AccountNotFoundException get() {
        return this;
    }
}
