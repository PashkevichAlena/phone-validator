package com.neotech.validator.utils.validator;

import com.neotech.validator.exception.PhoneValidatorException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.neotech.validator.exception.StatusCode.INVALID_CALLING_CODE;

public class CallingCodeValidator {
    private static final String CALLING_CODE_REGEXP = "(\\+\\d{1,7}( )?)?";
    private static final Pattern callingCodePattern = Pattern.compile(CALLING_CODE_REGEXP);

    public static void validate(String callingCode) {
        Matcher matcher = callingCodePattern.matcher(callingCode);
        if (!matcher.matches()) throw new PhoneValidatorException(INVALID_CALLING_CODE);
    }
}
