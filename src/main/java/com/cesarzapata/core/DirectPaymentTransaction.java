package com.cesarzapata.core;

public class DirectPaymentTransaction extends BaseTransaction {

    public DirectPaymentTransaction(Long id, String accountNumber, String sortCode, Money amount) {
        super(id, accountNumber, sortCode, amount);
    }

    public DirectPaymentTransaction(String accountNumber, String sortCode, Money amount) {
        super(accountNumber, sortCode, amount);
    }

    @Override
    public TransactionType type() {
        return TransactionType.DIRECT_PAYMENT;
    }
}
