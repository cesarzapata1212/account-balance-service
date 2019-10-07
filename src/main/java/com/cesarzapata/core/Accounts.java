package com.cesarzapata.core;

public interface Accounts {
    Account find(String accountNumber, String sortCode) throws AccountNotFoundException;

    void update(Account account) throws AccountNotFoundException;
}