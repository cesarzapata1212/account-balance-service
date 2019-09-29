package com.cesarzapata.feature;

import com.cesarzapata.App;
import com.cesarzapata.BalanceTransferRequest;
import com.cesarzapata.BalanceTransferRequest.Account;
import com.cesarzapata.support.AccountBalanceRepository;
import com.cesarzapata.support.AccountRepository;
import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BalanceTransferShould {

    private static App app;
    private static Javalin server;
    private AccountRepository accountRepository;
    private AccountBalanceRepository accountBalanceRepository;

    @BeforeClass
    public static void beforeClass() throws Exception {
        server = Javalin.create().start();
        app = new App(server, EmbeddedPostgres.start().getPostgresDatabase()).start();
    }

    @AfterClass
    public static void tearDown() {
        server.stop();
    }

    @Before
    public void beforeEach() {
        accountRepository = new AccountRepository(app.dataSource());
        accountBalanceRepository = new AccountBalanceRepository(app.dataSource());
    }

    @Test
    public void transfer_balance() throws Exception {
        // GIVEN
        BalanceTransferRequest req = new BalanceTransferRequest(
                new Account("11114444", "111444"),
                new Account("22225555", "222555"),
                "700"
        );
        createAccount(req.getSourceAccount(), new BigDecimal("1000.00"));
        createAccount(req.getDestinationAccount(), BigDecimal.ZERO);

        // WHEN
        ValidatableResponse response = given().config((RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL))))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON.getAcceptHeader())
                .body(req)
                .port(app.port())
                .when().post("/balance-transfer")
                .then();

        // THEN
        response.statusCode(HttpStatus.SC_OK)
                .body("sourceAccount.accountNumber", is("11114444"))
                .body("sourceAccount.sortCode", is("111444"))
                .body("sourceAccount.balance.value", is(new BigDecimal("300.00")))
                .body("destinationAccount.accountNumber", is("22225555"))
                .body("destinationAccount.sortCode", is("222555"))
                .body("destinationAccount.balance.value", is(new BigDecimal("700.00")));

        assertThat(accountBalanceRepository.selectBalance("11114444", "111444"), equalTo("300.00"));
        assertThat(accountBalanceRepository.selectBalance("22225555", "222555"), equalTo("700.00"));
    }

    @Test
    public void not_enough_balance() throws Exception {
        // GIVEN
        BalanceTransferRequest req = new BalanceTransferRequest(
                new Account("99998888", "999888"),
                new Account("88887777", "888777"),
                "1000"
        );
        createAccount(req.getSourceAccount(), new BigDecimal("500.00"));
        createAccount(req.getDestinationAccount(), BigDecimal.ZERO);

        // WHEN
        ValidatableResponse response = given().config((RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL))))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON.getAcceptHeader())
                .body(req)
                .port(app.port())
                .when().post("/balance-transfer")
                .then();

        // THEN
        response.statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                .body("status", is(422))
                .body("message", is("INSUFFICIENT_BALANCE"));

        assertThat(accountBalanceRepository.selectBalance("99998888", "999888"), equalTo("500.00"));
        assertThat(accountBalanceRepository.selectBalance("88887777", "888777"), equalTo("0.00"));
    }

    @Test
    public void account_not_found() {

    }

    private void createAccount(Account account, BigDecimal balance) throws SQLException {
        accountRepository.insert(account.getAccountNumber(), account.getSortCode());
        accountBalanceRepository.insert(account.getAccountNumber(), account.getSortCode(), balance);
    }
}

