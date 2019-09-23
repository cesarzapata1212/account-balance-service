package com.cesarzapata.support;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountBalanceRepository {
    private final DataSource dataSource;

    public AccountBalanceRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(String accountNumber, String sortCode, BigDecimal availableBalance) throws SQLException {
        String statement = "INSERT INTO account_balance (account_number, sort_code, available_balance) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement s = conn.prepareStatement(statement)) {
            s.setString(1, accountNumber);
            s.setString(2, sortCode);
            s.setBigDecimal(3, availableBalance);
            s.execute();
        }
    }

    public ResultSet select(String accountNumber, String sortCode) throws SQLException {
        return null;
    }
}
