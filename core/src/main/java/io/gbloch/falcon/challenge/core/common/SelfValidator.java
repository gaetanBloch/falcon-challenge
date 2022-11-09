package io.gbloch.falcon.challenge.core.common;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

public interface SelfValidator {
    default void validate() {
        ValidatorFactory factory = Validation.byDefaultProvider().configure().messageInterpolator(new ParameterMessageInterpolator()).buildValidatorFactory();
        var violations = factory.getValidator().validate(this);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(violations.iterator().next().getMessage());
        }
    }
}
