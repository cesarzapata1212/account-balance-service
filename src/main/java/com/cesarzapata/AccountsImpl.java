package com.cesarzapata;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.Outcome;

import javax.sql.DataSource;
import java.sql.SQLException;

public class AccountsImpl implements Accounts {
    private final DataSource dataSource;

    public AccountsImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Account find(String accountNumber, String sortCode) throws AccountNotFoundException {
        String sql = "SELECT a.account_number, a.sort_code, ab.available_balance FROM account a " +
                "LEFT JOIN account_balance ab ON a.account_number = ab.account_number AND a.sort_code = ab.sort_code " +
                "WHERE a.account_number = ? AND a.sort_code = ?";
        try {
            return new JdbcSession(dataSource).sql(sql)
                    .set(accountNumber)
                    .set(sortCode)
                    .select(new SingleAccountOutcome());
        } catch (SQLException e) {
            throw new AccountNotFoundException(accountNumber, sortCode, e);
        }
    }

    @Override
    public void update(Account account) throws AccountNotFoundException {
        String sql = "UPDATE account_balance SET available_balance = ? WHERE account_number = ? AND sort_code = ?";

        try {
            new JdbcSession(dataSource).sql(sql)
                    .set(account.balance().value())
                    .set(account.accountNumber())
                    .set(account.sortCode())
                    .update(Outcome.VOID);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }
}
