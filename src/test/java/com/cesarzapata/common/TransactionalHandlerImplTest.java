package com.cesarzapata.common;

import com.cesarzapata.support.AccountRepository;
import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.Outcome;
import com.opentable.db.postgres.embedded.FlywayPreparer;
import com.opentable.db.postgres.junit.EmbeddedPostgresRules;
import com.opentable.db.postgres.junit.PreparedDbRule;
import io.javalin.http.Context;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionalHandlerImplTest {
    @Rule
    public PreparedDbRule db = EmbeddedPostgresRules.preparedDatabase(FlywayPreparer.forClasspathLocation("database"));
    private AccountRepository accountRepository;

    @Before
    public void setUp() {
        accountRepository = new AccountRepository(db.getTestDatabase());
    }

    @Test
    public void should_rollback_and_rethrow_when_handler_fails() throws Exception {
        TransactionalHandler handler = (ctx, session) -> {
            insertAccount(session);
            throw new RuntimeException("Insert should be rolled back");
        };

        try {
            new TransactionalHandlerImpl(db.getTestDatabase(), handler).handle(context());
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), equalTo("Insert should be rolled back"));
        }

        assertThat(accountRepository.exists("111", "111"), is(false));
    }

    @Test
    public void should_commit_transaction_when_no_errors_are_thrown() throws Exception {
        TransactionalHandler handler = (ctx, session) -> insertAccount(session);

        new TransactionalHandlerImpl(db.getTestDatabase(), handler).handle(context());

        assertThat(accountRepository.exists("111", "111"), is(true));
    }

    @Test
    public void fail_to_connect_to_database() throws Exception {
        TransactionalHandler handler = mock(TransactionalHandler.class);
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getConnection()).thenThrow(new SQLException("Connection cannot be opened"));

        try {
            new TransactionalHandlerImpl(dataSource, handler).handle(context());
            fail();
        } catch (SQLException expected) {
            assertThat(expected.getMessage(), equalTo("Connection cannot be opened"));
        }
    }

    private void insertAccount(JdbcSession session) {
        try {
            Long id = session.sql("INSERT INTO account VALUES('111', '111')").insert(Outcome.LAST_INSERT_ID);
            assertTrue(id > 0L);
        } catch (SQLException e) {
            fail();
        }
    }

    private Context context() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        return new Context(req, res, new HashMap<>());
    }
}