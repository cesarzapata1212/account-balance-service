package com.cesarzapata;

public enum TransactionType {

    DIRECT_PAYMENT("DIRECT_PAYMENT"), DIRECT_DEPOSIT("DIRECT_DEPOSIT");

    private String value;

    TransactionType(String value) {
        this.value = value;
    }
}
