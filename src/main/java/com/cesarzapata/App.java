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

    public App start() throws SQLException {
        App app = new App();

        // Database
        DatabasePreparer preparer = FlywayPreparer.forClasspathLocation("database");
        PreparedDbProvider provider = PreparedDbProvider.forPreparer(preparer);
        app.dataSource = provider.createDataSource();

        // SERVER
        app.server = Javalin.create().start(port());

        JavalinJackson.configure(new ObjectMapper().setVisibility(PropertyAccessor.FIELD, Visibility.ANY));
        app.server.post("/balance-transfer", new BalanceTransferHandler(app.dataSource));
        return app;
    }

    public DataSource dataSource() {
        return dataSource;
    }

    public Integer port() {
        return 7777;
    }
}
