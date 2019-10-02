package com.cesarzapata;

import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;

public class TransactionsImpl implements Transactions {
    public TransactionsImpl(DataSource dataSource) {
    }

    @Override
    public void add(@NotNull Transaction transaction) {

    }
}
