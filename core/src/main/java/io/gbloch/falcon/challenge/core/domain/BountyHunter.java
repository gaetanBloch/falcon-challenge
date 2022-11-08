package io.gbloch.falcon.challenge.core.domain;


import io.gbloch.falcon.challenge.core.common.SelfValidator;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record BountyHunter(
    @NotNull(message = "The planet cannot be null")
    @NotEmpty(message = "The planet cannot be empty")
    String planet,
    @Min(value = 1, message = "The day must be positive")
    int day
) implements SelfValidator {

}
