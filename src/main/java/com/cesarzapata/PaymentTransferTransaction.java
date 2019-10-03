package com.cesarzapata;

public class PaymentTransferTransaction extends BaseTransaction {

    public PaymentTransferTransaction(Long id, String accountNumber, String sortCode, Money amount) {
        super(id, accountNumber, sortCode, amount);
    }

    public PaymentTransferTransaction(String accountNumber, String sortCode, Money amount) {
        super(accountNumber, sortCode, amount);
    }

    @Override
    public TransactionType type() {
        return TransactionType.PAYMENT_TRANSFER;
    }
}
