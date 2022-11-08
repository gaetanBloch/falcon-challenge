package io.gbloch.falcon.challenge.core.application.input.port;

import io.gbloch.falcon.challenge.core.domain.Empire;

public interface ComputeOddsUseCase {
    int whatAreTheOdds(String configFilePath, Empire empire);
}
