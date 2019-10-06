package com.cesarzapata;

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

        session.sql("COMMIT").execute();
    }

    private void rollback(JdbcSession session) throws SQLException {
        try {
            session.rollback();
        } catch (IllegalStateException e) {
            if (!e.getMessage().equals("connection is not open, can't rollback")) {
                throw e;
            }
        }
    }

    @NotNull
    private JdbcSession beginTransaction() throws SQLException {
        return new JdbcSession(dataSource)
                .autocommit(false)
                .sql("BEGIN TRANSACTION").execute();
    }
}
