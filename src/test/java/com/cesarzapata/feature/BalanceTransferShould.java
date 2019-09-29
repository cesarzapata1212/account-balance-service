package com.cesarzapata.feature;

import com.cesarzapata.App;
import com.cesarzapata.BalanceTransferRequest;
import com.cesarzapata.BalanceTransferRequest.Account;
import com.cesarzapata.support.AccountBalanceRepository;
import com.cesarzapata.support.AccountRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
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
    private AccountRepository accountRepository;
    private AccountBalanceRepository accountBalanceRepository;

    @BeforeClass
    public static void beforeClass() throws Exception {
        app = new App().start();
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
        response.statusCode(200)
                .body("sourceAccount.accountNumber", is("11114444"))
                .body("sourceAccount.sortCode", is("111444"))
                .body("sourceAccount.balance.value", is(new BigDecimal("300.00")))
                .body("destinationAccount.accountNumber", is("22225555"))
                .body("destinationAccount.sortCode", is("222555"))
                .body("destinationAccount.balance.value", is(new BigDecimal("700.00")));

        assertThat(accountBalanceRepository.selectBalance("11114444", "111444"), equalTo("300.00"));
        assertThat(accountBalanceRepository.selectBalance("22225555", "222555"), equalTo("700.00"));
    }

    private void createAccount(Account account, BigDecimal balance) throws SQLException {
        accountRepository.insert(account.getAccountNumber(), account.getSortCode());
        accountBalanceRepository.insert(account.getAccountNumber(), account.getSortCode(), balance);
    }
}

