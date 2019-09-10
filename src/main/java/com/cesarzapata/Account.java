package com.cesarzapata;

import java.math.BigDecimal;

public class Account {
    private final String sortCode;
    private final String accountNumber;
    private final Money balance;

    public Account(String sortCode, String accountNumber) {
        this(sortCode, accountNumber, new Money(BigDecimal.ZERO));
    }

    public Account(Account source, Money balance) {
        this(source.sortCode, source.accountNumber, balance);
    }

    public Account(String sortCode, String accountNumber, Money balance) {
        this.sortCode = sortCode;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public Money balance() {
        return this.balance;
    }
}
