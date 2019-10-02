package com.cesarzapata.support;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionRepository {
    private static final DateTimeFormatter POSTGRES_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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

        return new BigDecimal(
                new JdbcSession(dataSource)
                        .sql("SELECT amount FROM transaction WHERE account_number = ? AND sort_code = ?")
                        .set(accountNumber)
                        .set(sortCode)
                        .select(new SingleOutcome<>(String.class))
        );
    }

    public LocalDateTime selectDateTime(String accountNumber, String sortCode) throws SQLException {
        String d = new JdbcSession(dataSource)
                .sql("SELECT date_time::timestamp(0) FROM transaction WHERE account_number = ? AND sort_code = ?")
                .set(accountNumber)
                .set(sortCode)
                .select(new SingleOutcome<>(String.class));
        return LocalDateTime.parse(d, POSTGRES_DATE_TIME_FORMATTER);

    }
}
