package com.cesarzapata;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class BaseTransaction implements Transaction {

    private final Long id;
    private final String accountNumber;
    private final String sortCode;
    private final Money amount;
    private final LocalDateTime dateTime;

    protected BaseTransaction(Long id, String accountNumber, String sortCode, Money amount) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(accountNumber);
        Objects.requireNonNull(sortCode);
        Objects.requireNonNull(amount);
        this.id = id;
        this.accountNumber = accountNumber;
        this.sortCode = sortCode;
        this.amount = amount;
        this.dateTime = LocalDateTime.now().withNano(0);
    }

    protected BaseTransaction(String accountNumber, String sortCode, Money amount) {
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
        if (!(o instanceof BaseTransaction)) return false;
        BaseTransaction that = (BaseTransaction) o;
        return id.equals(that.id) &&
                accountNumber.equals(that.accountNumber) &&
                sortCode.equals(that.sortCode) &&
                amount.equals(that.amount) &&
                dateTime.equals(that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber, sortCode, amount, dateTime);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id())
                .append("accountNumber", accountNumber())
                .append("sortCode", sortCode())
                .append("type", type())
                .append("amount", amount())
                .append("dateTime", dateTime())
                .toString();
    }
}
