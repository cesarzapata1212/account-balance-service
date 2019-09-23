package com.cesarzapata;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountsImpl implements Accounts {
    private final DataSource dataSource;

    public AccountsImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Account find(String accountNumber, String sortCode) throws AccountNotFoundException {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement s = conn.prepareStatement(
                    "SELECT account_number, sort_code " +
                            "FROM account " +
                            "WHERE account_number = ? " +
                            "AND sort_code = ?");

            s.setString(1, accountNumber);
            s.setString(2, sortCode);

            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                return new Account(
                        rs.getString("account_number"),
                        rs.getString("sort_code")
                );
            } else {
                throw new IllegalArgumentException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void update(Account account) {

    }
}
