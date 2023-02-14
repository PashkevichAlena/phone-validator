package com.neotech.validator.exception;

import lombok.Getter;

@Getter
public class PhoneValidatorException extends RuntimeException {
    private final StatusCode statusCode;

    public PhoneValidatorException(StatusCode statusCode) {
        super(statusCode.defaultMessage);
        this.statusCode = statusCode;
    }
}
