package com.ias.quickfix.exceptions;

public class ProviderNotFoundException  extends RuntimeException {
    public ProviderNotFoundException(String message) {
        super(message);
    }
}

