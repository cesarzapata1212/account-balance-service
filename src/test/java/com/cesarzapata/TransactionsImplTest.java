package com.cesarzapata;

import com.cesarzapata.support.AccountRepository;
import com.cesarzapata.support.TransactionRepository;
import com.opentable.db.postgres.embedded.FlywayPreparer;
import com.opentable.db.postgres.junit.EmbeddedPostgresRules;
import com.opentable.db.postgres.junit.PreparedDbRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;

import static com.cesarzapata.support.CompareTo.compareTo;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class TransactionsImplTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Rule
    public PreparedDbRule db = EmbeddedPostgresRules.preparedDatabase(FlywayPreparer.forClasspathLocation("database"));
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    @Before
    public void setUp() {
        accountRepository = new AccountRepository(db.getTestDatabase());
        transactionRepository = new TransactionRepository(db.getTestDatabase());
    }

    @Test
    public void add_returns_transaction_id() throws SQLException {
        String accountNumber = "11114444";
        String sortCode = "111444";
        accountRepository.insert(accountNumber, sortCode);
        Transaction transaction = new PaymentTransferTransaction(accountNumber, sortCode, new Money("1"));

        Long id = new TransactionsImpl(db.getTestDatabase()).add(transaction);

        assertThat(id, notNullValue());
        assertThat(transactionRepository.selectId(accountNumber, sortCode), equalTo(id));
        assertThat(transactionRepository.selectType(accountNumber, sortCode), equalTo(transaction.type().toString()));
        assertThat(transactionRepository.selectAmount(accountNumber, sortCode), compareTo(transaction.amount().value()));
        assertThat(transactionRepository.selectDateTime(accountNumber, sortCode), compareTo(transaction.dateTime()));
    }

    @Test
    public void failed_insert() {
        Transaction transaction = new PaymentTransferTransaction("11114444", "111444", new Money("1"));

        thrown.expect(BusinessOperationException.class);
        thrown.expectMessage("Failed to insert transaction");

        new TransactionsImpl(db.getTestDatabase()).add(transaction); // throws no account constraint error
    }
    // CANNOT ADD transaction with id
}