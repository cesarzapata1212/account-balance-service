package com.cesarzapata;

public class DirectDepositTransaction extends BaseTransaction {

    public DirectDepositTransaction(Long id, String accountNumber, String sortCode, Money amount) {
        super(id, accountNumber, sortCode, amount);
    }

    public DirectDepositTransaction(String accountNumber, String sortCode, Money amount) {
        super(accountNumber, sortCode, amount);
    }

    @Override
    public TransactionType type() {
        return TransactionType.DIRECT_DEPOSIT;
    }
}
