package com.cesarzapata.core;

public class AccountNotFoundException extends RuntimeException {
    private final String accountNumber;
    private final String sortCode;

    public AccountNotFoundException(String accountNumber, String sortCode) {
        super("ACCOUNT_NOT_FOUND");
        this.accountNumber = accountNumber;
        this.sortCode = sortCode;
    }

    public AccountNotFoundException(String accountNumber, String sortCode, Exception e) {
        super("ACCOUNT_NOT_FOUND", e);
        this.accountNumber = accountNumber;
        this.sortCode = sortCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getSortCode() {
        return sortCode;
    }
}
