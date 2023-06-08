package com.example.instcrud.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    // the phone should be in the next format: +38 050 123-45-67
    private static final String PHONE_PATTERN =
            "^\\+38 \\d{3} \\d{3}-\\d{2}-\\d{2}$";

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        return phone == null || Pattern.matches(PHONE_PATTERN, phone);
    }
}
