package com.cesarzapata;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private final String sortCode;
    private final String accountNumber;
    private final Money balance;

    public Account(String accountNumber, String sortCode) {
        this(accountNumber, sortCode, new Money(BigDecimal.ZERO));
    }

    public Account(Account source, Money balance) {
        this(source.accountNumber, source.sortCode, balance);
    }

    public Account(String accountNumber, String sortCode, Money balance) {
        this.sortCode = sortCode;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public Money balance() {
        return this.balance;
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

    public String sortCode() {
        return sortCode;
    }

    public String accountNumber() {
        return accountNumber;
    }
}
