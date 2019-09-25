package com.cesarzapata.support;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.Outcome;
import com.jcabi.jdbc.SingleOutcome;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;

public class AccountBalanceRepository {
    private final DataSource dataSource;

    public AccountBalanceRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(String accountNumber, String sortCode, BigDecimal availableBalance) throws SQLException {
        Integer count = new JdbcSession(dataSource)
                .sql("INSERT INTO account_balance (account_number, sort_code, available_balance) VALUES (?, ?, ?)")
                .set(accountNumber)
                .set(sortCode)
                .set(availableBalance)
                .insert(Outcome.UPDATE_COUNT);
        if (count == 0) {
            throw new SQLException("Insert failed");
        }
    }

    public String selectBalance(String accountNumber, String sortCode) throws SQLException {
        String sql = "SELECT available_balance " +
                " FROM account_balance " +
                " WHERE account_number = ? " +
                " AND sort_code = ?";

        return new JdbcSession(dataSource)
                .sql(sql)
                .set(accountNumber)
                .set(sortCode)
                .select(new SingleOutcome<>(String.class));
    }
}
