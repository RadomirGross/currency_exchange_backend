package com.gross.currency_exchange_backend.exceptions;

import lombok.Getter;

@Getter
public class CustomServiceException extends RuntimeException {
    private final int errorCode;

    public CustomServiceException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
