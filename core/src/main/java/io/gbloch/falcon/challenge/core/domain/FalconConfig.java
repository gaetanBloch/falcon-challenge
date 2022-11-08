package io.gbloch.falcon.challenge.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.gbloch.falcon.challenge.core.common.SelfValidator;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record FalconConfig(
    @Min(value = 1, message = "The falcon's autonomy must be positive") int autonomy,
    @NotNull(message = "The falcon's departure planet must be defined")
    @NotEmpty(message = "The falcon's departure planet cannot be empty")
    String departure,
    @NotNull(message = "The falcon's arrival planet must be defined")
    @NotEmpty(message = "The falcon's arrival planet cannot be empty")
    String arrival,
    @NotNull(message = "The routes DB file path must be defined")
    @NotEmpty(message = "The routes DB file path planet cannot be empty")
    @JsonProperty("routes_db") String routesDbPath) implements SelfValidator {

}
