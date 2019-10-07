package com.cesarzapata.support;

import com.cesarzapata.core.Transaction;
import com.cesarzapata.core.Transactions;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class FakeTransactions implements Transactions {

    private final List<Transaction> transactions;

    public FakeTransactions() {
        this.transactions = new LinkedList<>();
    }

    public Transaction find(String accountNumber, String sortCode) {
        return transactions.stream()
                .filter(t -> t.accountNumber().equals(accountNumber) && t.sortCode().equals(sortCode))
                .findFirst()
                .orElseThrow(NullPointerException::new);
    }

    @Override
    public Long add(@NotNull Transaction transaction) {
        transactions.add(transaction);
        return Long.valueOf(transactions.indexOf(transaction));
    }
}
