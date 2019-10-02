package com.cesarzapata.support;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;

public class TransactionRepository {
    private DataSource dataSource;

    public TransactionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Long selectId(String accountNumber, String sortCode) throws SQLException {
        String sql = "SELECT id " +
                " FROM transaction " +
                " WHERE account_number = ? " +
                " AND sort_code = ?";

        return new JdbcSession(dataSource)
                .sql(sql)
                .set(accountNumber)
                .set(sortCode)
                .select(new SingleOutcome<>(Long.class));
    }

    public String selectType(String accountNumber, String sortCode) throws SQLException {
        String sql = "SELECT type " +
                " FROM transaction " +
                " WHERE account_number = ? " +
                " AND sort_code = ?";

        return new JdbcSession(dataSource)
                .sql(sql)
                .set(accountNumber)
                .set(sortCode)
                .select(new SingleOutcome<>(String.class));
    }

    public BigDecimal selectAmount(String accountNumber, String sortCode) throws SQLException {
        String sql = "SELECT amount " +
                " FROM transaction " +
                " WHERE account_number = ? " +
                " AND sort_code = ?";

        return new JdbcSession(dataSource)
                .sql(sql)
                .set(accountNumber)
                .set(sortCode)
                .select(new SingleOutcome<>(BigDecimal.class));
    }
}
