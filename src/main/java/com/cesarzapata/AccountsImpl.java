package com.cesarzapata;

import com.jcabi.jdbc.JdbcSession;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

import static java.util.Objects.requireNonNull;

public class AccountsImpl implements Accounts {

    private JdbcSession session;

    public AccountsImpl(@NotNull JdbcSession session) {
        this.session = requireNonNull(session);
    }

    @Override
    public Account find(String accountNumber, String sortCode) {
        String sql = "SELECT a.account_number, a.sort_code, ab.available_balance FROM account a " +
                "LEFT JOIN account_balance ab ON a.account_number = ab.account_number AND a.sort_code = ab.sort_code " +
                "WHERE a.account_number = ? AND a.sort_code = ?";
        try {
            return session
                    .sql(sql)
                    .set(accountNumber)
                    .set(sortCode)
                    .select(new SingleAccountOutcome());
        } catch (SQLException e) {
            throw new AccountNotFoundException(accountNumber, sortCode, e);
        }
    }

    @Override
    public void update(Account account) {
        Account a = find(account.accountNumber(), account.sortCode());
        try {
            session.sql("UPDATE account_balance SET available_balance = ? WHERE account_number = ? AND sort_code = ?")
                    .set(account.balance().value())
                    .set(a.accountNumber())
                    .set(a.sortCode())
                    .update(new UpdateOutcome());
        } catch (SQLException e) {
            throw new AccountNotFoundException(a.accountNumber(), a.sortCode(), e);
        }
    }
}
