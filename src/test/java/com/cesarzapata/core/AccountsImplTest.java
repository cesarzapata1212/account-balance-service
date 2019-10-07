package com.cesarzapata.core;

import com.cesarzapata.support.AccountBalanceRepository;
import com.cesarzapata.support.AccountRepository;
import com.jcabi.jdbc.JdbcSession;
import com.opentable.db.postgres.embedded.FlywayPreparer;
import com.opentable.db.postgres.junit.EmbeddedPostgresRules;
import com.opentable.db.postgres.junit.PreparedDbRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertThat;

public class AccountsImplTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Rule
    public PreparedDbRule db = EmbeddedPostgresRules.preparedDatabase(FlywayPreparer.forClasspathLocation("database"));
    private DataSource dataSource;
    private AccountRepository accountRepository;
    private AccountBalanceRepository accountBalanceRepository;

    @Before
    public void setUp() {
        dataSource = db.getTestDatabase();
        accountRepository = new AccountRepository(dataSource);
        accountBalanceRepository = new AccountBalanceRepository(dataSource);
    }

    @Test
    public void should_fetch_account_and_set_balance_to_zero() throws SQLException {
        accountRepository.insert("00001111", "000111");

        Account account = new AccountsImpl(new JdbcSession(dataSource)).find("00001111", "000111");

        assertThat(account, equalTo(new Account("00001111", "000111", new Money("0.00"))));
    }

    @Test
    public void should_fetch_account_with_balance() throws SQLException {
        BigDecimal balance = new BigDecimal("1000");
        accountRepository.insert("00001111", "0001111");
        accountBalanceRepository.insert("00001111", "0001111", balance);

        Account result = new AccountsImpl(new JdbcSession(dataSource)).find("00001111", "0001111");

        assertThat(result, equalTo(new Account("00001111", "0001111", new Money(balance))));
    }

    @Test
    public void should_fail_when_account_is_not_found() {
        thrown.expect(AccountNotFoundException.class);
        thrown.expectMessage("ACCOUNT_NOT_FOUND");
        thrown.expect(hasProperty("accountNumber", is("00000000")));
        thrown.expect(hasProperty("sortCode", is("000000")));

        new AccountsImpl(new JdbcSession(dataSource)).find("00000000", "000000");
    }

    @Test
    public void should_update_account() throws SQLException {
        accountRepository.insert("00001111", "000111");
        accountBalanceRepository.insert("00001111", "000111", BigDecimal.ZERO);

        new AccountsImpl(new JdbcSession(dataSource)).update(new Account("00001111", "000111", new Money("100")));

        assertThat(accountBalanceRepository.selectBalance("00001111", "000111"), equalTo("100.00"));
    }

    @Test
    public void should_fail_when_updated_account_is_not_found() {
        thrown.expect(AccountNotFoundException.class);
        thrown.expectMessage("ACCOUNT_NOT_FOUND");
        thrown.expect(hasProperty("accountNumber", is("00000000")));
        thrown.expect(hasProperty("sortCode", is("000000")));

        new AccountsImpl(new JdbcSession(dataSource)).update(new Account("00000000", "000000", new Money("100")));
    }
}