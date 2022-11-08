package io.gbloch.falcon.challenge.core.application.service;

import com.google.common.graph.MutableValueGraph;
import io.gbloch.falcon.challenge.core.application.FalconCoreException;
import io.gbloch.falcon.challenge.core.application.input.port.ComputeOddsUseCase;
import io.gbloch.falcon.challenge.core.application.service.db.GalaxyDbException;
import io.gbloch.falcon.challenge.core.application.service.db.GalaxyDbFileReader;
import io.gbloch.falcon.challenge.core.application.service.parser.FalconFileException;
import io.gbloch.falcon.challenge.core.application.service.parser.FalconFileParser;
import io.gbloch.falcon.challenge.core.domain.Empire;
import io.gbloch.falcon.challenge.core.domain.FalconConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
final class OddsService implements ComputeOddsUseCase {

    final FalconFileParser falconFileParser;
    final GalaxyDbFileReader galaxyDbFileReader;

    Empire empire;
    FalconConfig falconConfig;
    MutableValueGraph<String, Integer> galaxy;

    @Override
    public int whatAreTheOdds(String configFilePath, Empire empire) throws FalconCoreException {
        empire.validate();
        try {
            log.info("Computing odds for empire {}...", empire);
            this.empire = empire;
            this.falconConfig = falconFileParser.parseFile(configFilePath);
            this.galaxy = galaxyDbFileReader.readFile(falconConfig.routesDbPath());
        } catch (FalconFileException | GalaxyDbException e) {
            throw new FalconCoreException(e);
        } catch (Exception e) {
            log.error("Unexpected error", e);
            throw new FalconCoreException("Unexpected exception", e);
        }
        log.info("Computing odds for empire {}... OK", empire);
        return -1;
    }
}