package com.cesarzapata;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class SystemProperties {

    private static final String DATASOURCE_CONFIG_ENV = "DATASOURCE_CONFIG_FILE";

    public Properties load() {
        return loadFromEnv().orElseGet(this::loadDefault);
    }

    private Properties loadDefault() {
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("default.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            return prop;
        } catch (IOException e) {
            throw new RuntimeException("Default datasource properties file not found");
        }
    }

    private Optional<Properties> loadFromEnv() {
        String filePath = System.getProperty(DATASOURCE_CONFIG_ENV);
        if (isEmpty(filePath)) {
            return Optional.empty();
        }
        try (InputStream input = new FileInputStream(filePath)) {
            Properties prop = new Properties();
            prop.load(input);
            return Optional.of(prop);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config file provided", e);
        }
    }
}
