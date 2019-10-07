package com.cesarzapata.core;

import java.time.LocalDateTime;

public interface Transaction {

    Long id();

    String accountNumber();

    String sortCode();

    TransactionType type();

    Money amount();

    LocalDateTime dateTime();

}
