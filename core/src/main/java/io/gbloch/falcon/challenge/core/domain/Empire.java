package io.gbloch.falcon.challenge.core.domain;

import io.gbloch.falcon.challenge.core.common.SelfValidator;
import java.util.Set;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public record Empire(
    @Min(value = 1, message = "The countdown must be positive") int countdown,
    @NotNull(message = "The bounty hunters must be defined") Set<BountyHunter> bountyHunters
) implements SelfValidator {

}
