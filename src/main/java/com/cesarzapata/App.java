package com.cesarzapata;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentable.db.postgres.embedded.DatabasePreparer;
import com.opentable.db.postgres.embedded.FlywayPreparer;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

public class App {

    private static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    private DataSource dataSource;
    private Javalin server;

    public App(Javalin server, DataSource dataSource) {
        this.server = server;
        this.dataSource = dataSource;
    }

    public App start() throws SQLException {
        initDb(this);
        initServer(this);
        return this;
    }

    public DataSource dataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource has not been initialized");
        }
        return dataSource;
    }

    public int port() {
        if (server == null) {
            throw new IllegalStateException("Server has not been initialized");
        }
        return server.port();
    }

    private void initServer(App app) {
        JavalinJackson.configure(DEFAULT_MAPPER);
        app.server.post("/balance-transfer", new BalanceTransferHandler(new AccountsImpl(app.dataSource())));
        app.server.exception(BusinessOperationException.class, new UnprocessableEntityExceptionHandler());
        app.server.exception(AccountNotFoundException.class, new UnprocessableEntityExceptionHandler());
        app.server.exception(IllegalArgumentException.class, new UnprocessableEntityExceptionHandler());
    }

    private void initDb(App app) throws SQLException {
        DatabasePreparer preparer = FlywayPreparer.forClasspathLocation("database");
        preparer.prepare(app.dataSource);
    }
}
