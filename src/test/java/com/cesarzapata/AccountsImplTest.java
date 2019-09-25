package com.cesarzapata;

import com.cesarzapata.support.AccountBalanceRepository;
import com.cesarzapata.support.AccountRepository;
import com.opentable.db.postgres.embedded.FlywayPreparer;
import com.opentable.db.postgres.junit.EmbeddedPostgresRules;
import com.opentable.db.postgres.junit.PreparedDbRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class AccountsImplTest {

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
        String accountNumber = "00001111";
        String sortCode = "000111";
        accountRepository.insert(accountNumber, sortCode);

        Account account = new AccountsImpl(dataSource).find(accountNumber, sortCode);

        assertThat(account, equalTo(new Account(accountNumber, sortCode, new Money("0.00"))));
    }

    @Test
    public void should_fetch_account_with_balance() throws SQLException {
        String accountNumber = "00001111";
        String sortCode = "0001111";
        BigDecimal balance = new BigDecimal("1000");
        accountRepository.insert(accountNumber, sortCode);
        accountBalanceRepository.insert(accountNumber, sortCode, balance);

        Account result = new AccountsImpl(dataSource).find(accountNumber, sortCode);

        assertThat(result, equalTo(new Account(accountNumber, sortCode, new Money(balance))));
    }

    @Test(expected = AccountNotFoundException.class)
    public void should_fail_if_account_not_found() {
        new AccountsImpl(dataSource).find("000", "000");
    }

    @Test
    public void should_update_account() throws SQLException {
        String accountNumber = "00001111";
        String sortCode = "000111";
        accountRepository.insert(accountNumber, sortCode);
        accountBalanceRepository.insert(accountNumber, sortCode, BigDecimal.ZERO);

        new AccountsImpl(dataSource).update(new Account(accountNumber, sortCode, new Money("100")));

        List<String[]> rows = accountBalanceRepository.select(accountNumber, sortCode);

        assertThat(rows.size(), equalTo(1));
        String[] row = rows.get(0);
        assertThat(row[0], equalTo(accountNumber));
        assertThat(row[1], equalTo(sortCode));
        assertThat(row[2], equalTo("100.00"));
    }
}