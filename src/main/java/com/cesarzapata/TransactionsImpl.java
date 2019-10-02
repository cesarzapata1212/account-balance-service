package com.cesarzapata;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.Outcome;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.SQLException;

public class TransactionsImpl implements Transactions {
    private final DataSource dataSource;

    public TransactionsImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Long add(@NotNull Transaction t) {
        try {
            return new JdbcSession(dataSource)
                    .sql("INSERT INTO transaction (account_number, sort_code, type, amount, date_time) " +
                            " VALUES (?, ?, ?::transaction_type, ?, ?)")
                    .set(t.accountNumber())
                    .set(t.sortCode())
                    .set(t.type().toString())
                    .set(t.amount().value())
                    .set(t.dateTime())
                    .insert(Outcome.LAST_INSERT_ID);
        } catch (SQLException e) {
            throw new BusinessOperationException("Failed to insert transaction", e);
        }
    }
}
