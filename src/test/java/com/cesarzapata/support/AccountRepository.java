package com.cesarzapata.support;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.Outcome;

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
            throw new SQLException("Insert failed");
        }
    }
}
