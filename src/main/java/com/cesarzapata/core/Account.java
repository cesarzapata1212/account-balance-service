package com.cesarzapata.core;

import java.util.Objects;

public class Account {
    private final String sortCode;
    private final String accountNumber;
    private final Money balance;

    public Account(String accountNumber, String sortCode, Money balance) {
        Objects.requireNonNull(accountNumber);
        Objects.requireNonNull(sortCode);
        Objects.requireNonNull(balance);
        this.sortCode = sortCode;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public Account(String accountNumber, String sortCode) {
        this(accountNumber, sortCode, Money.ZERO);
    }

    private Account(Account source, Money balance) {
        this(source.accountNumber, source.sortCode, balance);
    }

    public Money balance() {
        return this.balance;
    }

    public String sortCode() {
        return sortCode;
    }

    public String accountNumber() {
        return accountNumber;
    }

    public Account balance(Money amount) {
        return new Account(this, amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return sortCode.equals(account.sortCode) &&
                accountNumber.equals(account.accountNumber) &&
                balance.equals(account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sortCode, accountNumber, balance);
    }

    @Override
    public String toString() {
        return "Account{" +
                "sortCode='" + sortCode + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                '}';
    }
}
