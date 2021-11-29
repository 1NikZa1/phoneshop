package com.es.core.exception;

public class OutOfStockException extends RuntimeException {

    public OutOfStockException(){}
    public OutOfStockException(String message) {
        super(message);
    }
}
