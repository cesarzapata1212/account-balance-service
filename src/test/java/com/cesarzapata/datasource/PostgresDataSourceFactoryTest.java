package com.cesarzapata.datasource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Properties;

import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;

public class PostgresDataSourceFactoryTest {

    @Test
    public void fromProperties() {
        Properties properties = new Properties();
        properties.setProperty("datasource.serverName", "localhost1");
        properties.setProperty("datasource.portNumber", "4444");
        properties.setProperty("datasource.databaseName", "mydb");
        properties.setProperty("datasource.user", "myuser1");
        properties.setProperty("datasource.password", "mypass1");
        properties.setProperty("datasource.loginTimeout", "15");
        properties.setProperty("datasource.socketTimeout", "15");

        PGSimpleDataSource result = PostgresDataSourceFactory.fromProperties(properties);

        assertThat(result.getServerName(), equalTo("localhost1"));
        assertThat(result.getPortNumber(), equalTo(4444));
        assertThat(result.getDatabaseName(), equalTo("mydb"));
        assertThat(result.getUser(), equalTo("myuser1"));
        assertThat(result.getPassword(), equalTo("mypass1"));
        assertThat(result.getLoginTimeout(), equalTo(15));
        assertThat(result.getSocketTimeout(), equalTo(15));
    }

    @Test
    public void fromProperties_2() {
        Properties properties = new Properties();
        properties.setProperty("datasource.serverName", "127.0.0.1");
        properties.setProperty("datasource.portNumber", "8080");
        properties.setProperty("datasource.databaseName", "mydatabase");
        properties.setProperty("datasource.user", "anotheruser");
        properties.setProperty("datasource.password", "anotherpass");
        properties.setProperty("datasource.loginTimeout", "20");
        properties.setProperty("datasource.socketTimeout", "20");

        PGSimpleDataSource result = PostgresDataSourceFactory.fromProperties(properties);

        assertThat(result.getServerName(), equalTo("127.0.0.1"));
        assertThat(result.getPortNumber(), equalTo(8080));
        assertThat(result.getDatabaseName(), equalTo("mydatabase"));
        assertThat(result.getUser(), equalTo("anotheruser"));
        assertThat(result.getPassword(), equalTo("anotherpass"));
        assertThat(result.getLoginTimeout(), equalTo(20));
        assertThat(result.getSocketTimeout(), equalTo(20));
    }

    @Test
    public void default_values() {
        Properties properties = new Properties();

        PGSimpleDataSource result = PostgresDataSourceFactory.fromProperties(properties);

        assertThat(result.getServerName(), equalTo("localhost"));
        assertThat(result.getPortNumber(), equalTo(5432));
        assertThat(result.getDatabaseName(), equalTo("postgres"));
        assertThat(result.getUser(), equalTo("postgres"));
        assertThat(result.getPassword(), equalTo("postgres"));
        assertThat(result.getLoginTimeout(), equalTo(10));
        assertThat(result.getSocketTimeout(), equalTo(10));
    }
}
