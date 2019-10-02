package com.cesarzapata;

import java.time.LocalDateTime;
import java.util.Objects;

public class PaymentTransferTransaction implements Transaction {

    private final String accountNumber;
    private final String sortCode;
    private final Long id;
    private final Money amount;
    private final LocalDateTime dateTime;

    public PaymentTransferTransaction(Long id, String accountNumber, String sortCode, Money amount) {
        Objects.requireNonNull(accountNumber);
        Objects.requireNonNull(sortCode);
        Objects.requireNonNull(amount);
        this.id = id;
        this.accountNumber = accountNumber;
        this.sortCode = sortCode;
        this.amount = amount;
        this.dateTime = LocalDateTime.now().withNano(0);
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
    public TransactionType type() {
        return TransactionType.PAYMENT_TRANSFER;
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
        if (!(o instanceof PaymentTransferTransaction)) return false;
        PaymentTransferTransaction that = (PaymentTransferTransaction) o;
        return accountNumber.equals(that.accountNumber) &&
                sortCode.equals(that.sortCode) &&
                Objects.equals(id, that.id) &&
                amount.equals(that.amount) &&
                dateTime.equals(that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, sortCode, id, amount, dateTime);
    }

    @Override
    public String toString() {
        return "PaymentTransferTransaction{" +
                "accountNumber='" + accountNumber + '\'' +
                ", sortCode='" + sortCode + '\'' +
                ", id=" + id +
                ", amount=" + amount +
                ", dateTime=" + dateTime +
                '}';
    }
}
