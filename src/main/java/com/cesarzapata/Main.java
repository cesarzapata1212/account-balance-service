package com.cesarzapata;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.cesarzapata.datasource.PostgresDataSourceFactory;
import io.javalin.Javalin;
import org.jetbrains.annotations.NotNull;
import org.postgresql.ds.PGSimpleDataSource;

public class Main {

    public static void main(String[] args) throws SQLException, IOException {
        Javalin server = Javalin.create().start();
        DataSource dataSource = getDataSource();

        new App(server, dataSource).start();

        System.out.println("Application started: " + server.server().server().getURI().toString());
    }

    @NotNull
    private static PGSimpleDataSource getDataSource() throws IOException {
        Properties properties = new SystemProperties().load();
        return PostgresDataSourceFactory.fromProperties(properties);
    }
}
