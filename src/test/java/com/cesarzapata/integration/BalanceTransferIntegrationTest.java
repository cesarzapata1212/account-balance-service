package com.cesarzapata.integration;

import com.cesarzapata.App;
import com.opentable.db.postgres.embedded.FlywayPreparer;
import com.opentable.db.postgres.junit.EmbeddedPostgresRules;
import com.opentable.db.postgres.junit.PreparedDbRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;

public class BalanceTransferIntegrationTest {

    @Rule
    public PreparedDbRule db = EmbeddedPostgresRules.preparedDatabase(
            FlywayPreparer.forClasspathLocation("database"));

    private App app;

    @Before
    public void setUp() {
        app = new App().start();
    }


    // set up 2 accounts in the DB

    // set up an HTTP endpoint

    // given account 1 has 1000 balance, and account 2 has 0 (ZERO) balance

    // when I call the endpoint to make a transfer from account 1 to account 2 in the value of 300

    // then account 1 should have remaining balance of 700

    // account 2 should have new balance of 300

    @Test
    public void balanceTransfer() throws SQLException {
        // create a DB connection
        // insert test data

        // start application server

        try (Connection conn = db.getTestDatabase().getConnection(); Statement statement = conn.createStatement()) {

            statement.execute("INSERT INTO account_balance VALUES ('111111', '11111111', 1000.0)");

            ResultSet rs = statement.executeQuery("select * from account_balance");
            rs.next();
            assertEquals("1000.00", rs.getString("available_balance"));
        }
    }
}
