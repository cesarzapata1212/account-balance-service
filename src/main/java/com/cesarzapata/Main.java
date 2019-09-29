package com.cesarzapata;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import io.javalin.Javalin;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException, IOException {
        Javalin server = Javalin.create().start();
        DataSource dataSource = EmbeddedPostgres.start().getPostgresDatabase();

        new App(server, dataSource).start();

        System.out.println("Application started: " + server.server().server().getURI().toString());
    }
}
