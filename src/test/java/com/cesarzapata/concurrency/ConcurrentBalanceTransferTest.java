package com.cesarzapata.concurrency;

import com.cesarzapata.Account;
import com.cesarzapata.AccountsImpl;
import com.cesarzapata.BalanceTransferHandler;
import com.cesarzapata.BalanceTransferRequest;
import com.cesarzapata.ConcurrentAccountUpdateException;
import com.cesarzapata.Money;
import com.cesarzapata.TransactionalHandlerImpl;
import com.cesarzapata.support.AccountBalanceRepository;
import com.cesarzapata.support.AccountRepository;
import com.cesarzapata.support.TestContext;
import com.jcabi.jdbc.JdbcSession;
import com.opentable.db.postgres.embedded.FlywayPreparer;
import com.opentable.db.postgres.junit.EmbeddedPostgresRules;
import com.opentable.db.postgres.junit.PreparedDbRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ConcurrentBalanceTransferTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Rule
    public PreparedDbRule db = EmbeddedPostgresRules.preparedDatabase(FlywayPreparer.forClasspathLocation("database"));
    private AccountRepository accountRepository;
    private AccountBalanceRepository balanceRepository;

    @Before
    public void setUp() {
        accountRepository = new AccountRepository(db.getTestDatabase());
        balanceRepository = new AccountBalanceRepository(db.getTestDatabase());
    }

    @Test
    public void concurrent_updates_to_the_same_account_should_commit_first_transaction_only() throws Exception {
        // Given
        accountRepository.insert("11110000", "111000");
        balanceRepository.insert("11110000", "111000", new BigDecimal("1000"));
        accountRepository.insert("22220000", "222000");
        balanceRepository.insert("22220000", "222000", BigDecimal.ZERO);

        // When concurrent transactions execute
        JdbcSession transaction1 = new JdbcSession(db.getTestDatabase()).autocommit(false).sql("BEGIN TRANSACTION").execute();
        new BalanceTransferHandler().handle(
                new TestContext(new BalanceTransferRequest(
                        new BalanceTransferRequest.Account("11110000", "111000"),
                        new BalanceTransferRequest.Account("22220000", "222000"),
                        new BigDecimal("800")
                )).create(), transaction1);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                TransactionalHandlerImpl transaction2 = new TransactionalHandlerImpl(db.getTestDatabase(), new BalanceTransferHandler());
                transaction2.handle(
                        new TestContext(new BalanceTransferRequest(
                                new BalanceTransferRequest.Account("11110000", "111000"),
                                new BalanceTransferRequest.Account("22220000", "222000"),
                                new BigDecimal("500")
                        )).create());
            } catch (Exception e) {
            }
        });
        executorService.awaitTermination(1, TimeUnit.SECONDS);
        transaction1.commit();

        // Then first transaction initiated persists only
        assertThat(balanceRepository.selectBalance("11110000", "111000"), equalTo("200.00"));
        assertThat(balanceRepository.selectBalance("22220000", "222000"), equalTo("800.00"));
    }

    @Test
    public void repository_should_throw_concurrent_update_error() throws Throwable {
        // Given
        accountRepository.insert("00001111", "000111");
        balanceRepository.insert("00001111", "000111", BigDecimal.ZERO);

        // When
        JdbcSession transaction1 = new JdbcSession(db.getTestDatabase()).autocommit(false).sql("SET TRANSACTION ISOLATION LEVEL SERIALIZABLE").execute();
        new AccountsImpl(transaction1).update(new Account("00001111", "000111", new Money("100")));

        Callable<Object> transaction2 = () -> {
            JdbcSession transaction = new JdbcSession(db.getTestDatabase()).autocommit(false).sql("SET TRANSACTION ISOLATION LEVEL SERIALIZABLE").execute();
            new AccountsImpl(transaction).update(new Account("00001111", "000111", new Money("200")));
            transaction.commit();
            return null;
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future expected = executor.submit(transaction2);
        executor.awaitTermination(1, TimeUnit.SECONDS);

        transaction1.commit();

        // Then
        thrown.expect(ConcurrentAccountUpdateException.class);
        thrown.expectMessage("Concurrent update to account failed");
        try {
            expected.get();
            fail();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }
}
