package com.cesarzapata;

import com.cesarzapata.support.AccountRepository;
import com.jcabi.jdbc.JdbcSession;
import com.opentable.db.postgres.embedded.FlywayPreparer;
import com.opentable.db.postgres.junit.EmbeddedPostgresRules;
import com.opentable.db.postgres.junit.PreparedDbRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeFalse;

public class UpdateOutcomeTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Rule
    public PreparedDbRule db = EmbeddedPostgresRules.preparedDatabase(FlywayPreparer.forClasspathLocation("database"));
    private AccountRepository accountRepository;

    @Before
    public void setUp() {
        accountRepository = new AccountRepository(db.getTestDatabase());
    }

    @Test
    public void should_return_affected_rows() throws SQLException {
        accountRepository.insert("11110000", "000111");

        Integer affectedRows = new JdbcSession(db.getTestDatabase())
                .sql("UPDATE account SET account_number = 1 WHERE account_number = ? AND sort_code = ?")
                .set("11110000")
                .set("000111")
                .update(new UpdateOutcome());

        assertThat(affectedRows, is(1));
    }

    @Test
    public void should_fail_when_update_count_is_zero() throws SQLException {
        assumeFalse(accountRepository.exists("0", "0"));

        thrown.expect(SQLException.class);
        thrown.expectMessage("Invalid update statement. 0 records updated.");

        new JdbcSession(db.getTestDatabase())
                .sql("UPDATE account SET account_number = 1 WHERE account_number = ? AND sort_code = ?")
                .set("0")
                .set("0")
                .update(new UpdateOutcome());
    }
}