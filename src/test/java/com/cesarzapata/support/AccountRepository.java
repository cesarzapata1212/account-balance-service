package com.cesarzapata.support;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.Outcome;
import com.jcabi.jdbc.SingleOutcome;

import javax.sql.DataSource;
import java.sql.SQLException;

public class AccountRepository {
    private final DataSource dataSource;

    public AccountRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(String accountNumber, String sortCode) throws SQLException {
        Integer count = new JdbcSession(dataSource).sql("INSERT INTO account (account_number, sort_code) VALUES (?, ?)")
                .set(accountNumber)
                .set(sortCode)
                .insert(Outcome.UPDATE_COUNT);
        if (count == 0) {
            throw new SQLException("INSERT failed");
        }
    }

    public boolean exists(String accountNumber, String sortCode) throws SQLException {
        Long count = new JdbcSession(dataSource).sql("SELECT COUNT(1) FROM account WHERE account_number = ? AND sort_code = ?")
                .set(accountNumber)
                .set(sortCode)
                .select(new SingleOutcome<>(Long.class));
        return count > 0;
    }
}
