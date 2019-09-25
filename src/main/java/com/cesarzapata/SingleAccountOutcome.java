package com.cesarzapata;

import com.jcabi.jdbc.Outcome;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static java.math.BigDecimal.ZERO;

public class SingleAccountOutcome implements Outcome<Account> {

    @Override
    public Account handle(ResultSet rs, Statement s) throws SQLException {
        if (!rs.next()) {
            throw new SQLException("Expected one record in the resultSet but found none");
        }
        if (!rs.isLast()) {
            throw new SQLException("Expected a single record but got multiple");
        }
        return new Account(
                rs.getString("account_number"),
                rs.getString("sort_code"),
                new Money(Optional.ofNullable(rs.getBigDecimal("available_balance")).orElse(ZERO))
        );
    }
}