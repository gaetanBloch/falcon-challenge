package io.gbloch.falcon.challenge.core.application.service;

import io.gbloch.falcon.challenge.core.application.FalconCoreException;
import io.gbloch.falcon.challenge.core.application.input.port.ComputeOddsUseCase;
import io.gbloch.falcon.challenge.core.application.service.parser.FalconFileException;
import io.gbloch.falcon.challenge.core.application.service.parser.FalconFileParser;
import io.gbloch.falcon.challenge.core.domain.Empire;
import io.gbloch.falcon.challenge.core.domain.FalconConfig;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class OddsService implements ComputeOddsUseCase {

    final FalconFileParser falconFileParser;

    FalconConfig falconConfig;

    @Override
    public int whatAreTheOdds(String configFilePath, Empire empire) throws FalconCoreException {
        try {
            falconConfig = falconFileParser.parseFile(configFilePath);
        } catch (FalconFileException e) {
            throw new FalconCoreException(e);
        }
        return -1;
    }
}
