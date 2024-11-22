package org.example.utils;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class ValidatorUtil {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    public static <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            StringBuilder errors = new StringBuilder("Erro(s) de validação:\n");
            for (ConstraintViolation<T> violation : violations) {
                errors.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("\n");
            }
            throw new IllegalArgumentException(errors.toString());
        }
    }
}

