package com.cesarzapata;

public enum TransactionType {

    PAYMENT_TRANSFER("PAYMENT_TRANSFER");

    private String value;

    TransactionType(String value) {
        this.value = value;
    }
}
