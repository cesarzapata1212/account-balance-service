package com.cesarzapata;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentable.db.postgres.embedded.DatabasePreparer;
import com.opentable.db.postgres.embedded.FlywayPreparer;
import com.opentable.db.postgres.embedded.PreparedDbProvider;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

public class App {

    private DataSource dataSource;
    private Javalin server;
    private ObjectMapper om = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

    public App start() throws SQLException {
        App app = new App();
        initDb(app);
        initServer(app);
        return app;
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
        app.server = Javalin.create().start();
        JavalinJackson.configure(om);
        app.server.post("/balance-transfer", new BalanceTransferHandler(app.dataSource));
    }

    private void initDb(App app) throws SQLException {
        DatabasePreparer preparer = FlywayPreparer.forClasspathLocation("database");
        PreparedDbProvider provider = PreparedDbProvider.forPreparer(preparer);
        app.dataSource = provider.createDataSource();
    }
}
