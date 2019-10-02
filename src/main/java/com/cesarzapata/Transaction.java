package com.cesarzapata;

import java.time.LocalDateTime;

public interface Transaction {

    String accountNumber();

    String sortCode();

    TransactionType type();

    Money amount();

    LocalDateTime dateTime();

}
