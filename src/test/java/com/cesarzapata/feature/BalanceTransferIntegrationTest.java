package com.cesarzapata.feature;

import com.cesarzapata.App;
import com.cesarzapata.BalanceTransferRequest;
import com.cesarzapata.BalanceTransferRequest.Account;
import com.cesarzapata.support.AccountBalanceRepository;
import com.cesarzapata.support.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
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
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class BalanceTransferIntegrationTest {

    private static App app;
    private AccountRepository accountRepository;
    private AccountBalanceRepository accountBalanceRepository;

    @BeforeClass
    public static void setUp() throws SQLException {
        app = new App().start();
    }

    @Before
    public void beforeEach() {
        accountRepository = new AccountRepository(app.dataSource());
        accountBalanceRepository = new AccountBalanceRepository(app.dataSource());
    }

    @Test
    public void appShouldBeInitialized() {
        assertNotNull(app);
    }

    @Test
    public void appShouldInitializeDataSource() {
        assertNotNull(app.dataSource());
    }

    @Test
    public void appShouldReturnPort() {
        assertThat(app.port(), equalTo(7777));
    }

    @Test
    public void balanceTransfer() throws IOException, SQLException {
        // GIVEN
        String sourceAccountNumber = "11114444";
        String sourceSortCode = "111444";
        createAccount(sourceAccountNumber, sourceSortCode, new BigDecimal("1000.00"));

        String destinationAccountNumber = "22225555";
        String destinationSortCode = "222555";
        createAccount(destinationAccountNumber, destinationSortCode, BigDecimal.ZERO);

        // WHEN
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://localhost:" + app.port() + "/balance-transfer");
        post.setHeader("Accept", "application/json");

        BalanceTransferRequest transferRequest = new BalanceTransferRequest(
                new Account(sourceAccountNumber, sourceSortCode),
                new Account(destinationAccountNumber, destinationSortCode),
                "700"
        );
        StringEntity requestEntity = new StringEntity(new ObjectMapper().writeValueAsString(transferRequest), ContentType.APPLICATION_JSON);
        post.setEntity(requestEntity);
        HttpResponse response = httpClient.execute(post);

        // THEN
        assertThat(response.getStatusLine().getStatusCode(), equalTo(200));

        List<String[]> sourceBalance = accountBalanceRepository.select(sourceAccountNumber, sourceSortCode);
        assertThat(sourceBalance.size(), equalTo(1));
        assertThat(sourceBalance.get(0)[0], equalTo(sourceAccountNumber));
        assertThat(sourceBalance.get(0)[1], equalTo(sourceSortCode));
        assertThat(sourceBalance.get(0)[2], equalTo("300.00"));

        List<String[]> destinationAccountBalance = accountBalanceRepository.select(destinationAccountNumber, destinationSortCode);
        assertThat(destinationAccountBalance.size(), equalTo(1));
        assertThat(destinationAccountBalance.get(0)[0], equalTo(destinationAccountNumber));
        assertThat(destinationAccountBalance.get(0)[1], equalTo(destinationSortCode));
        assertThat(destinationAccountBalance.get(0)[2], equalTo("700.00"));
    }

    private void createAccount(String sourceAccountNumber, String sourceSortCode, BigDecimal availableBalance) throws SQLException {
        accountRepository.insert(sourceAccountNumber, sourceSortCode);
        accountBalanceRepository.insert(sourceAccountNumber, sourceSortCode, availableBalance);
    }
}

