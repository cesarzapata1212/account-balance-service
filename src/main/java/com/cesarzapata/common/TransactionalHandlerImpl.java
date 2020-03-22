package com.cesarzapata.common;

import com.jcabi.jdbc.JdbcSession;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.SQLException;

import static java.util.Objects.requireNonNull;

public class TransactionalHandlerImpl implements Handler {
    private final DataSource dataSource;
    private final TransactionalHandler handler;

    public TransactionalHandlerImpl(@NotNull DataSource dataSource, @NotNull TransactionalHandler handler) {
        this.dataSource = requireNonNull(dataSource);
        this.handler = requireNonNull(handler);
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        JdbcSession session = beginTransaction();

        try {
            handler.handle(ctx, session);
        } catch (Exception e) {
            rollback(session);
            throw e;
        }

        commit(session);
    }

    private void commit(JdbcSession session) throws SQLException {
        session.commit();
    }

    private void rollback(JdbcSession session) throws SQLException {
        try {
            session.rollback();
        } catch (IllegalStateException e) {
            // connection is not open, can't rollback ignored
        }
    }

    @NotNull
    private JdbcSession beginTransaction() throws SQLException {
        return new JdbcSession(dataSource)
                .autocommit(false)
                .sql("BEGIN TRANSACTION ISOLATION LEVEL READ COMMITTED").execute();
    }
}
