package com.cesarzapata.concurrency;

import com.cesarzapata.BalanceTransferHandler;
import com.cesarzapata.BalanceTransferRequest;
import com.cesarzapata.TransactionalHandlerImpl;
import com.cesarzapata.support.AccountBalanceRepository;
import com.cesarzapata.support.AccountRepository;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcabi.jdbc.JdbcSession;
import com.opentable.db.postgres.embedded.FlywayPreparer;
import com.opentable.db.postgres.junit.EmbeddedPostgresRules;
import com.opentable.db.postgres.junit.PreparedDbRule;
import io.javalin.http.Context;
import io.javalin.plugin.json.JavalinJackson;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class ConcurrentBalanceTransferTest {

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
                context(new BalanceTransferRequest(
                        new BalanceTransferRequest.Account("11110000", "111000"),
                        new BalanceTransferRequest.Account("22220000", "222000"),
                        new BigDecimal("800")
                )), transaction1);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                TransactionalHandlerImpl transaction2 = new TransactionalHandlerImpl(db.getTestDatabase(), new BalanceTransferHandler());
                transaction2.handle(
                        context(new BalanceTransferRequest(
                                new BalanceTransferRequest.Account("11110000", "111000"),
                                new BalanceTransferRequest.Account("22220000", "222000"),
                                new BigDecimal("500")
                        )));
            } catch (Exception e) {
            }
        });
        executorService.awaitTermination(1, TimeUnit.SECONDS);
        transaction1.commit();

        // Then first transaction initiated persists only
        assertThat(balanceRepository.selectBalance("11110000", "111000"), equalTo("200.00"));
        assertThat(balanceRepository.selectBalance("22220000", "222000"), equalTo("800.00"));
    }


    private Context context(Object payload) {
        HttpServletRequest req = mockServletPayload(payload);
        HttpServletResponse res = Mockito.mock(HttpServletResponse.class);
        Context context = new Context(req, res, new HashMap<>());
        ObjectMapper objectMapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        JavalinJackson.configure(objectMapper);
        return context;
    }

    private HttpServletRequest mockServletPayload(Object payload) {
        HttpServletRequest result = Mockito.mock(HttpServletRequest.class);
        ObjectMapper objectMapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsString(payload).getBytes();
            InputStream is = new ByteArrayInputStream(bytes);
            when(result.getInputStream()).thenReturn(new MockServletInputStream(is));
        } catch (IOException e) {
            fail(e.getMessage());
        }

        return result;
    }

    class MockServletInputStream extends ServletInputStream {
        private InputStream inputStream;

        public MockServletInputStream(InputStream is) {
            inputStream = is;
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }
    }
}
