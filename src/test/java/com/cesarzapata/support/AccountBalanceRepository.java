package com.cesarzapata.support;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountBalanceRepository {
    private final DataSource dataSource;

    public AccountBalanceRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(String accountNumber, String sortCode, BigDecimal availableBalance) throws SQLException {
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {

        }
    }

    public ResultSet select(String accountNumber, String sortCode) {
        return null;
    }
}
