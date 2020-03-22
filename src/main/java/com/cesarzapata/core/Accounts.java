package com.cesarzapata.core;

import java.math.BigDecimal;

public interface Accounts {
    Account find(String accountNumber, String sortCode) throws AccountNotFoundException;

    void update(Account account, final BigDecimal previousBalance) throws AccountNotFoundException;
}
