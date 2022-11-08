package io.gbloch.falcon.challenge.core.common;

import javax.validation.Validation;

public interface SelfValidator {
    default void validate() {
        var violations = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(violations.iterator().next().getMessage());
        }
    }
}
