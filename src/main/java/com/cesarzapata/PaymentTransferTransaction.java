package com.cesarzapata;

import java.util.Objects;

public class PaymentTransferTransaction implements Transaction {

    private final String accountNumber;
    private final String sortCode;
    private final Long id;
    private final Money amount;

    public PaymentTransferTransaction(Long id, String accountNumber, String sortCode, Money amount) {
        Objects.requireNonNull(accountNumber);
        Objects.requireNonNull(sortCode);
        Objects.requireNonNull(amount);
        this.id = id;
        this.accountNumber = accountNumber;
        this.sortCode = sortCode;
        this.amount = amount;
    }

    public PaymentTransferTransaction(String accountNumber, String sortCode, Money amount) {
        this(0L, accountNumber, sortCode, amount);
    }

    @Override
    public String accountNumber() {
        return this.accountNumber;
    }

    @Override
    public String sortCode() {
        return this.sortCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentTransferTransaction)) return false;
        PaymentTransferTransaction that = (PaymentTransferTransaction) o;
        return accountNumber.equals(that.accountNumber) &&
                sortCode.equals(that.sortCode) &&
                Objects.equals(id, that.id) &&
                amount.equals(that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, sortCode, id, amount);
    }

    @Override
    public String toString() {
        return "PaymentTransferTransaction{" +
                "accountNumber='" + accountNumber + '\'' +
                ", sortCode='" + sortCode + '\'' +
                ", id=" + id +
                ", amount=" + amount +
                '}';
    }
}
