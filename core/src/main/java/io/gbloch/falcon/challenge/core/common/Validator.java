package io.gbloch.falcon.challenge.core.common;

import javax.validation.Validation;

public final class Validator {
    private Validator() {
        throw new UnsupportedOperationException(
            "This is a utility class and cannot be instantiated");
    }

    public static void validate(Object object) {
        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        validator.validate(object).forEach((violation) -> {
            throw new IllegalArgumentException(violation.getMessage());
        });
    }
}
