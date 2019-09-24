package com.cesarzapata.integration;

import com.cesarzapata.App;
import com.cesarzapata.support.AccountBalanceRepository;
import com.cesarzapata.support.AccountRepository;
import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class BalanceTransferIntegrationTest {

    private static App app;

    @BeforeClass
    public static void setUp() throws IOException, SQLException {
        app = new App().start();
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

    // set up 2 accounts in the DB
    // set up an HTTP endpoint
    // given account 1 has 1000 balance, and account 2 has 0 (ZERO) balance
    // when I call the endpoint to make a transfer from account 1 to account 2 in the value of 300
    // then account 1 should have remaining balance of 700
    // account 2 should have new balance of 300
    @Test
    public void balanceTransfer() throws IOException, SQLException {
        // GIVEN
        AccountRepository accountRepository = new AccountRepository(app.dataSource());
        AccountBalanceRepository accountBalanceRepository = new AccountBalanceRepository(app.dataSource());

        String sourceAccountNumber = RandomStringUtils.randomNumeric(8);
        String sourceSortCode = RandomStringUtils.randomNumeric(6);
        accountRepository.insert(sourceAccountNumber, sourceSortCode);
        accountBalanceRepository.insert(sourceAccountNumber, sourceSortCode, new BigDecimal("1000.00"));

        String destinationAccountNumber = RandomStringUtils.randomNumeric(8);
        String destinationSortCode = RandomStringUtils.randomNumeric(6);
        accountRepository.insert(destinationAccountNumber, destinationSortCode);
        accountBalanceRepository.insert(destinationAccountNumber, destinationSortCode, BigDecimal.ZERO);

        // WHEN
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://localhost:" + app.port() + "/balance-transfer");
        post.setHeader("Accept", "application/json");


        StringEntity requestEntity = new StringEntity(
                String.format("{" +
                                "\"sourceAccount\":{\"accountNumber\":\"%s\",\"sortCode\":\"%s\"}," +
                                "\"destinationAccount\":{\"accountNumber\":\"%s\",\"sortCode\":\"%s\"}," +
                                "\"amount\":%s" +
                                "}",
                        sourceAccountNumber,
                        sourceSortCode,
                        destinationAccountNumber,
                        destinationSortCode,
                        "700"
                ),
                ContentType.APPLICATION_JSON);
        post.setEntity(requestEntity);
        HttpResponse response = httpClient.execute(post);
        HttpEntity entity = response.getEntity();
        Header encodingHeader = entity.getContentEncoding();
        Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 : Charsets.toCharset(encodingHeader.getValue());
        String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);

        System.out.println(json);
        // THEN
        assertThat(response.getStatusLine().getStatusCode(), equalTo(200));

        String statement = "SELECT account_number, sort_code, available_balance " +
                "FROM account_balance " +
                "WHERE account_number = ? " +
                "AND sort_code = ?";

        try (Connection conn = app.dataSource().getConnection()) {
            PreparedStatement sourceStmt = conn.prepareStatement(statement);
            sourceStmt.setString(1, sourceAccountNumber);
            sourceStmt.setString(2, sourceSortCode);
            ResultSet sourceAccountBalance = sourceStmt.executeQuery();

            PreparedStatement destinationStmt = conn.prepareStatement(statement);
            destinationStmt.setString(1, destinationAccountNumber);
            destinationStmt.setString(2, destinationSortCode);
            ResultSet destinationAccountBalance = destinationStmt.executeQuery();

            assertTrue(sourceAccountBalance.next());
            assertThat(sourceAccountBalance.getString("available_balance"), equalTo("300.00"));
            assertFalse(sourceAccountBalance.next());

            assertTrue(destinationAccountBalance.next());
            assertThat(destinationAccountBalance.getString("available_balance"), equalTo("700.00"));
            assertFalse(destinationAccountBalance.next());
        }
    }
}

