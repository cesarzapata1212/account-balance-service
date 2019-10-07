package com.cesarzapata.support;

import com.cesarzapata.core.Account;
import com.cesarzapata.core.AccountNotFoundException;
import com.cesarzapata.core.Accounts;

import java.util.List;
import java.util.stream.Collectors;

public class FakeAccounts implements Accounts {

    private List<Account> accounts;

    public FakeAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public Account find(String accountNumber, String sortCode) throws AccountNotFoundException {
        return accounts.stream()
                .filter(account -> account.accountNumber().equals(accountNumber) && account.sortCode().equals(sortCode))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException(accountNumber, sortCode));
    }

    @Override
    public void update(Account account) {
        accounts = accounts.stream().map(existingAccount ->
                existingAccount.accountNumber().equals(account.accountNumber()) &&
                        existingAccount.sortCode().equals(account.sortCode())
                        ? account
                        : existingAccount
        ).collect(Collectors.toList());
    }
}
