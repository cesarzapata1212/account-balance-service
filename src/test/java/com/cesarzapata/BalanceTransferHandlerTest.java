package com.cesarzapata;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BalanceTransferHandlerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private Accounts accounts;
    @Mock
    private Transactions transactions;
    @Mock
    private HttpServletResponse res;
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
    public void validate_source_account() throws Exception {
        payload.setSourceAccount(null);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid sourceAccount provided");

        balanceTransferHandler.handle(createContext());
    }

    @Test
    public void validate_source_account_number() throws Exception {
        payload.getSourceAccount().setAccountNumber("");

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid sourceAccount.accountNumber provided");

        balanceTransferHandler.handle(createContext());
    }

    @Test
    public void validate_source_account_sort_code() throws Exception {
        payload.getSourceAccount().setSortCode("");

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid sourceAccount.sortCode provided");

        balanceTransferHandler.handle(createContext());
    }

    @Test
    public void validate_destination_account() throws Exception {
        payload.setDestinationAccount(null);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid destinationAccount provided");

        balanceTransferHandler.handle(createContext());
    }

    @Test
    public void validate_destination_account_number() throws Exception {
        payload.getDestinationAccount().setAccountNumber("");

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid destinationAccount.accountNumber provided");

        balanceTransferHandler.handle(createContext());
    }

    @Test
    public void validate_destination_account_sort_code() throws Exception {
        payload.getDestinationAccount().setSortCode("");

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid destinationAccount.sortCode provided");

        balanceTransferHandler.handle(createContext());
    }

    @Test
    public void validate_amount() throws Exception {
        payload.setAmount(null);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid amount provided");

        balanceTransferHandler.handle(createContext());
    }

    private Context createContext() throws Exception {
        HttpServletRequest req = mockServletPayload();
        return new Context(req, res, new HashMap<>());
    }

    private HttpServletRequest mockServletPayload() throws Exception {
        HttpServletRequest result = Mockito.mock(HttpServletRequest.class);

        byte[] bytes = new ObjectMapper().writeValueAsString(payload).getBytes();
        InputStream is = new ByteArrayInputStream(bytes);
        when(result.getInputStream()).thenReturn(new MockServletInputStream(is));

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