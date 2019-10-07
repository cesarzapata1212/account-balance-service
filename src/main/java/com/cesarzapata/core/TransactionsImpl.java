package com.cesarzapata.core;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.Outcome;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

import static java.util.Objects.requireNonNull;

public class TransactionsImpl implements Transactions {
    private final JdbcSession session;

    public TransactionsImpl(@NotNull JdbcSession session) {
        this.session = requireNonNull(session);
    }

    @Override
    public Long add(@NotNull Transaction t) {
        if (t.id() != 0L) {
            throw new BusinessOperationException("Invalid transaction id provided");
        }
        try {
            return session
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
