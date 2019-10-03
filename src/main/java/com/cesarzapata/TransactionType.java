package com.cesarzapata;

public enum TransactionType {

    PAYMENT_TRANSFER("PAYMENT_TRANSFER"), DIRECT_DEPOSIT("DIRECT_DEPOSIT");

    private String value;

    TransactionType(String value) {
        this.value = value;
    }
}
