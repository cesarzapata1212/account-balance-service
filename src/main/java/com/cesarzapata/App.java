package com.cesarzapata;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import io.javalin.Javalin;

import javax.sql.DataSource;
import java.io.IOException;

public class App {

    private DataSource dataSource;
    private Javalin server;

    public App start() throws IOException {
        App app = new App();
        app.dataSource = EmbeddedPostgres.start().getPostgresDatabase();
        app.server = Javalin.create().start(port());
        app.server.post("/balance-transfer", new BalanceTransferHandler());
        return app;
    }

    public DataSource dataSource() {
        return dataSource;
//        FlywayPreparer.forClasspathLocation(DATABASE_SCRIPTS_FOLDER).prepare(dataSource);
    }

    public Integer port() {
        return 7777;
    }
}
