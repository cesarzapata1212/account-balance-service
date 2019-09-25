package com.cesarzapata.feature;

import com.cesarzapata.App;
import com.cesarzapata.BalanceTransferRequest;
import com.cesarzapata.BalanceTransferRequest.Account;
import com.cesarzapata.support.AccountBalanceRepository;
import com.cesarzapata.support.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class BalanceTransferShould {

    private static App app;
    private AccountRepository accountRepository;
    private AccountBalanceRepository accountBalanceRepository;

    @BeforeClass
    public static void beforeClass() throws SQLException {
        app = new App().start();
    }

    @Before
    public void beforeEach() {
        accountRepository = new AccountRepository(app.dataSource());
        accountBalanceRepository = new AccountBalanceRepository(app.dataSource());
    }

    @Test
    public void transfer_balance() throws IOException, SQLException {
        // GIVEN
        BalanceTransferRequest req = new BalanceTransferRequest(
                new Account("11114444", "111444"),
                new Account("22225555", "222555"),
                "700"
        );
        createAccount(req.getSourceAccount(), new BigDecimal("1000.00"));
        createAccount(req.getDestinationAccount(), BigDecimal.ZERO);

        // WHEN
        CloseableHttpResponse response = makeBalanceTransferHttpRequest(req);

        // THEN
        assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
        assertThat(accountBalanceRepository.selectBalance(
                req.getSourceAccount().getAccountNumber(),
                req.getSourceAccount().getSortCode()), equalTo("300.00"));

        assertThat(accountBalanceRepository.selectBalance(
                req.getDestinationAccount().getAccountNumber(),
                req.getDestinationAccount().getSortCode()), equalTo("700.00"));
    }

    private CloseableHttpResponse makeBalanceTransferHttpRequest(BalanceTransferRequest req) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://localhost:" + app.port() + "/balance-transfer");
        post.setHeader("Accept", "application/json");
        StringEntity requestEntity = new StringEntity(new ObjectMapper().writeValueAsString(req), ContentType.APPLICATION_JSON);
        post.setEntity(requestEntity);
        return httpClient.execute(post);
    }

    private void createAccount(Account account, BigDecimal balance) throws SQLException {
        accountRepository.insert(account.getAccountNumber(), account.getSortCode());
        accountBalanceRepository.insert(account.getAccountNumber(), account.getSortCode(), balance);
    }
}

