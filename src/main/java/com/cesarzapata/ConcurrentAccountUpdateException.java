package com.cesarzapata;

public class ConcurrentAccountUpdateException extends RuntimeException {

    public ConcurrentAccountUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
