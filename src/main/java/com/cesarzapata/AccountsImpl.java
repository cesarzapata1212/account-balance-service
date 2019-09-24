package com.cesarzapata;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AccountsImpl implements Accounts {
    private final DataSource dataSource;

    public AccountsImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Account find(String accountNumber, String sortCode) throws AccountNotFoundException {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement s = conn.prepareStatement(
                    "SELECT a.account_number, a.sort_code, ab.available_balance " +
                            "FROM account a " +
                            "LEFT JOIN account_balance ab " +
                            "ON a.account_number = ab.account_number AND a.sort_code = ab.sort_code " +
                            "WHERE a.account_number = ? " +
                            "AND a.sort_code = ?");

            s.setString(1, accountNumber);
            s.setString(2, sortCode);

            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                return new Account(
                        rs.getString("account_number"),
                        rs.getString("sort_code"),
                        new Money(Optional.ofNullable(rs.getBigDecimal("available_balance"))
                                .orElse(BigDecimal.ZERO)
                        ));
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
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement s = conn.prepareStatement(
                    "UPDATE account_balance " +
                            " SET available_balance = ?" +
                            " WHERE account_number = ? " +
                            " AND sort_code = ?");

            s.setBigDecimal(1, account.balance().value());
            s.setString(2, account.accountNumber());
            s.setString(3, account.sortCode());

            s.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }
}
