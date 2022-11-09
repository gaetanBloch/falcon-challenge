package io.gbloch.falcon.challenge.core.common;


import javax.validation.Validator;

public interface SelfValidator {

    default void validate(Validator validator) {
        var violations = validator.validate(this);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(violations.iterator().next().getMessage());
        }
    }
}
