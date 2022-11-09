package io.gbloch.falcon.challenge.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.gbloch.falcon.challenge.core.common.SelfValidator;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.Set;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RegisterForReflection
public record Empire(
    @Min(value = 1, message = "The countdown must be positive") int countdown,
    @NotNull(message = "The bounty hunters must be defined")
    @JsonProperty("bounty_hunters")
    Set<BountyHunter> bountyHunters
) implements SelfValidator {

}
