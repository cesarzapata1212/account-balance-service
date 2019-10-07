package com.cesarzapata.common;

import com.jcabi.jdbc.Outcome;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateOutcome implements Outcome<Integer> {
    @Override
    public Integer handle(ResultSet rs, Statement s) throws SQLException {
        int count = s.getUpdateCount();
        if (count == 0) {
            throw new SQLException("Invalid update statement. 0 records updated.");
        }
        return count;
    }
}
