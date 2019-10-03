package com.cesarzapata;

import java.time.LocalDateTime;
import java.util.Objects;

public class DirectDepositTransaction implements Transaction {

    private final Long id;
    private final String accountNumber;
    private final String sortCode;
    private final Money amount;
    private final LocalDateTime dateTime;

    public DirectDepositTransaction(Long id, String accountNumber, String sortCode, Money amount) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.sortCode = sortCode;
        this.amount = amount;
        this.dateTime = LocalDateTime.now().withNano(0);
    }

    public DirectDepositTransaction(String accountNumber, String sortCode, Money amount) {
        this(0L, accountNumber, sortCode, amount);
    }

    @Override
    public Long id() {
        return id;
    }

    @Override
    public String accountNumber() {
        return accountNumber;
    }

    @Override
    public String sortCode() {
        return sortCode;
    }

    @Override
    public TransactionType type() {
        return TransactionType.DIRECT_DEPOSIT;
    }

    @Override
    public Money amount() {
        return amount;
    }

    @Override
    public LocalDateTime dateTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectDepositTransaction)) return false;
        DirectDepositTransaction that = (DirectDepositTransaction) o;
        return Objects.equals(id, that.id) &&
                accountNumber.equals(that.accountNumber) &&
                sortCode.equals(that.sortCode) &&
                amount.equals(that.amount) &&
                dateTime.equals(that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber, sortCode, amount, dateTime);
    }
}
