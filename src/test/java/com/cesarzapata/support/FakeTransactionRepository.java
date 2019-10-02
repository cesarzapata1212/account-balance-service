package com.cesarzapata.support;

import com.cesarzapata.Transaction;
import com.cesarzapata.Transactions;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class FakeTransactionRepository implements Transactions {

    private final List<Transaction> transactions;

    public FakeTransactionRepository() {
        this.transactions = new LinkedList<>();
    }

    public Transaction find(String accountNumber, String sortCode) {
        return transactions.stream()
                .filter(t -> t.accountNumber().equals(accountNumber) && t.sortCode().equals(sortCode))
                .findFirst()
                .orElseThrow(NullPointerException::new);
    }

    @Override
    public void add(@NotNull Transaction transaction) {
        transactions.add(transaction);
    }
}
