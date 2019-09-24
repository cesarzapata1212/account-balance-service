package com.cesarzapata.support;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountRepository {
    private final DataSource dataSource;

    public AccountRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(String accountNumber, String sortCode) throws SQLException {
        String stmt = "INSERT INTO account (account_number, sort_code) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement s = conn.prepareStatement(stmt);
            s.setString(1, accountNumber);
            s.setString(2, sortCode);
            s.execute();
        }
    }
}
