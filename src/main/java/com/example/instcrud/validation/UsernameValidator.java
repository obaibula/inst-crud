package com.example.instcrud.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {
    // should contain only lowercase letters or numbers
    // and at least 8 characters long and shorter than 32
    private static final String USERNAME_PATTERN =
            "^[a-z0-9]{8,16}$";

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return username != null && Pattern.matches(USERNAME_PATTERN, username);
    }
}
