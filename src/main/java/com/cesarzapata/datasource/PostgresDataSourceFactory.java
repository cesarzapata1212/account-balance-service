package com.cesarzapata.datasource;

import java.util.Properties;

import com.cesarzapata.common.util.OptionalInt;
import com.cesarzapata.common.util.OptionalString;
import org.postgresql.ds.PGSimpleDataSource;

public class PostgresDataSourceFactory {

    private static final Integer DEFAULT_PORT = 5432;
    private static final Integer DEFAULT_TIMEOUT = 10;
    private static final String DEFAULT_DATABASE_NAME = "postgres";
    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASSWORD = "postgres";

    public static PGSimpleDataSource fromProperties(final Properties properties) {
        PGSimpleDataSource source = new PGSimpleDataSource();
        source.setServerName(properties.getProperty("datasource.serverName"));
        source.setPortNumber(new OptionalInt(properties.getProperty("datasource.portNumber")).orElse(DEFAULT_PORT));
        source.setDatabaseName(new OptionalString(properties.getProperty("datasource.databaseName")).orElse(DEFAULT_DATABASE_NAME));
        source.setUser(new OptionalString(properties.getProperty("datasource.user")).orElse(DEFAULT_USER));
        source.setPassword(new OptionalString(properties.getProperty("datasource.password")).orElse(DEFAULT_PASSWORD));
        source.setLoginTimeout(new OptionalInt(properties.getProperty("datasource.loginTimeout")).orElse(DEFAULT_TIMEOUT));
        source.setSocketTimeout(new OptionalInt(properties.getProperty("datasource.socketTimeout")).orElse(DEFAULT_TIMEOUT));
        return source;
    }
}
