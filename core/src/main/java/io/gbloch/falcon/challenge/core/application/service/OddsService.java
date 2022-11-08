package io.gbloch.falcon.challenge.core.application.service;

import io.gbloch.falcon.challenge.core.application.input.port.ComputeOddsUseCase;
import io.gbloch.falcon.challenge.core.domain.Empire;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class OddsService implements ComputeOddsUseCase {

    @Override
    public int whatAreTheOdds(String configFilePath, Empire empire) {
        return -1;
    }
}
