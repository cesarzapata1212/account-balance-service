package com.cesarzapata;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.junit.After;
import org.junit.Test;

public class SystemPropertiesTest {

    private static Path TEST_FOLDER = Paths.get("src").resolve("test").resolve("resources").resolve("systemproperties");

    @Test
    public void load_from_environment_variable() {
        String testConfigFile = TEST_FOLDER.resolve("test-config.properties").toString();
        System.setProperty("DATASOURCE_CONFIG_FILE", testConfigFile);

        Properties properties = new SystemProperties().load();

        assertThat(properties.getProperty("datasource.serverName"), equalTo("serverName"));
        assertThat(properties.getProperty("datasource.portNumber"), equalTo("1234"));
        assertThat(properties.getProperty("datasource.databaseName"), equalTo("databaseName"));
        assertThat(properties.getProperty("datasource.user"), equalTo("user"));
        assertThat(properties.getProperty("datasource.password"), equalTo("password"));
        assertThat(properties.getProperty("datasource.loginTimeout"), equalTo("123"));
        assertThat(properties.getProperty("datasource.socketTimeout"), equalTo("321"));
    }

    @Test(expected = RuntimeException.class)
    public void should_throw_error_when_file_provided_cant_be_loaded() {
        String testConfigFile = Paths.get("an_invalid_path_to_file").toString();
        System.setProperty("DATASOURCE_CONFIG_FILE", testConfigFile);

        new SystemProperties().load();
    }

    @Test
    public void load_from_default_file_when_environment_variable_is_not_present() {
        Properties properties = new SystemProperties().load();

        assertThat(properties.getProperty("datasource.serverName"), equalTo("localhost"));
        assertThat(properties.getProperty("datasource.portNumber"), equalTo("5432"));
        assertThat(properties.getProperty("datasource.databaseName"), equalTo("postgres"));
        assertThat(properties.getProperty("datasource.user"), equalTo("postgres"));
        assertThat(properties.getProperty("datasource.password"), equalTo("postgres"));
        assertThat(properties.getProperty("datasource.loginTimeout"), equalTo("10"));
        assertThat(properties.getProperty("datasource.socketTimeout"), equalTo("10"));
    }

    @After
    public void tearDown() {
        System.getProperties().remove("DATASOURCE_CONFIG_FILE");
    }
}