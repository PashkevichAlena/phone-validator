package com.neotech.validator.utils.validator;

import com.neotech.validator.exception.PhoneValidatorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CallingCodeValidatorTest {

    @Test
    public void validateCallingCodeIsOkay() {
        CallingCodeValidator.validate("+123");
    }

    @Test
    public void validateCallingCodeWithSpecialSymbolsMustThrowException() {
        assertThrows(
                PhoneValidatorException.class,
                () -> CallingCodeValidator.validate("!@##)%*)*#")
        );
    }

    @Test
    public void validateCallingCodeWithAlphabetSymbolsMustThrowException() {
        assertThrows(
                PhoneValidatorException.class,
                () -> CallingCodeValidator.validate("+23asd")
        );
    }

    @Test
    public void validateCallingCodeWithLotsOfSymbolsMustThrowException() {
        assertThrows(
                PhoneValidatorException.class,
                () -> CallingCodeValidator.validate("+123456789")
        );
    }
}