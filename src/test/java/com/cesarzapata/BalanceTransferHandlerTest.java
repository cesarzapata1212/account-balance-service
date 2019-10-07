package com.cesarzapata;

import com.cesarzapata.support.TestContext;
import com.jcabi.jdbc.JdbcSession;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.math.BigDecimal;

@RunWith(MockitoJUnitRunner.class)
public class BalanceTransferHandlerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private DataSource dataSource;
    @InjectMocks
    private BalanceTransferHandler balanceTransferHandler;
    private BalanceTransferRequest payload;

    @Before
    public void setUp() {
        payload = new BalanceTransferRequest(
                new BalanceTransferRequest.Account("1", "1"),
                new BalanceTransferRequest.Account("2", "2"),
                new BigDecimal("1000")
        );
    }

    @Test
    public void validate_source_account() {
        payload.setSourceAccount(null);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid sourceAccount provided");

        balanceTransferHandler.handle(new TestContext(payload).create(), new JdbcSession(dataSource));
    }

    @Test
    public void validate_source_account_number() {
        payload.getSourceAccount().setAccountNumber("");

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid sourceAccount.accountNumber provided");

        balanceTransferHandler.handle(new TestContext(payload).create(), new JdbcSession(dataSource));
    }

    @Test
    public void validate_source_account_sort_code() {
        payload.getSourceAccount().setSortCode("");

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid sourceAccount.sortCode provided");

        balanceTransferHandler.handle(new TestContext(payload).create(), new JdbcSession(dataSource));
    }

    @Test
    public void validate_destination_account() {
        payload.setDestinationAccount(null);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid destinationAccount provided");

        balanceTransferHandler.handle(new TestContext(payload).create(), new JdbcSession(dataSource));
    }

    @Test
    public void validate_destination_account_number() {
        payload.getDestinationAccount().setAccountNumber("");

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid destinationAccount.accountNumber provided");

        balanceTransferHandler.handle(new TestContext(payload).create(), new JdbcSession(dataSource));
    }

    @Test
    public void validate_destination_account_sort_code() {
        payload.getDestinationAccount().setSortCode("");

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid destinationAccount.sortCode provided");

        balanceTransferHandler.handle(new TestContext(payload).create(), new JdbcSession(dataSource));
    }

    @Test
    public void validate_amount() {
        payload.setAmount(null);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid amount provided");

        balanceTransferHandler.handle(new TestContext(payload).create(), new JdbcSession(dataSource));
    }
}