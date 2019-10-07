package com.cesarzapata.core;

import com.jcabi.jdbc.JdbcSession;
import com.opentable.db.postgres.embedded.FlywayPreparer;
import com.opentable.db.postgres.junit.EmbeddedPostgresRules;
import com.opentable.db.postgres.junit.PreparedDbRule;
import org.junit.Rule;
import org.junit.Test;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SingleAccountOutcomeTest {

    @Rule
    public PreparedDbRule db = EmbeddedPostgresRules.preparedDatabase(FlywayPreparer.forClasspathLocation("database"));

    @Test
    public void should_parse_all_fields() throws SQLException {
        Account account = new JdbcSession(db.getTestDatabase())
                .sql("SELECT '111' as account_number, '222' as sort_code, 100.00 as available_balance")
                .select(new SingleAccountOutcome());

        assertThat(account, equalTo(new Account("111", "222", new Money("100"))));
    }

    @Test
    public void available_balance_not_present_should_set_zero_balance() throws SQLException {
        Account account = new JdbcSession(db.getTestDatabase())
                .sql("SELECT '111' as account_number, '222' as sort_code, null as available_balance")
                .select(new SingleAccountOutcome());

        assertThat(account, equalTo(new Account("111", "222", new Money("0"))));
    }

    @Test(expected = SQLException.class)
    public void multiple_rows_should_throw_error() throws SQLException {
        new JdbcSession(db.getTestDatabase())
                .sql("SELECT '111' as account_number, '222' as sort_code, null as available_balance " +
                        "UNION ALL SELECT '111' as account_number, '222' as sort_code, null as available_balance")
                .select(new SingleAccountOutcome());
    }
}