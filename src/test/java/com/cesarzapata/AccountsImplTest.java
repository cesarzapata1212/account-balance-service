package com.cesarzapata;

import com.opentable.db.postgres.embedded.FlywayPreparer;
import com.opentable.db.postgres.junit.EmbeddedPostgresRules;
import com.opentable.db.postgres.junit.PreparedDbRule;
import org.junit.Rule;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class AccountsImplTest {

    @Rule
    public PreparedDbRule db = EmbeddedPostgresRules.preparedDatabase(FlywayPreparer.forClasspathLocation("database"));

    @Test
    public void should_find_existing_account() throws SQLException {
        DataSource dataSource = db.getTestDatabase();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("insert into account (account_number, sort_code) " +
                    " VALUES ('00001111','000111')");
        }

        Account account = new AccountsImpl(dataSource).find("00001111", "000111");

        assertThat(account, equalTo(new Account("00001111", "000111")));
    }

    @Test
    public void update() {
    }
}