package com.neotech.validator.exception;

import lombok.Getter;

@Getter
public enum StatusCode {

    DATA_NOT_RECEIVED(1, "Data not received from source"),
    COUNTRY_NOT_FOUND(2, "This code unassigned to country"),
    INVALID_CALLING_CODE(3, "Calling code is invalid");

    StatusCode(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    final int code;
    final String defaultMessage;
}
