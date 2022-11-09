package io.gbloch.falcon.challenge.core.application.service;

import com.google.common.graph.MutableValueGraph;
import io.gbloch.falcon.challenge.core.application.service.db.GalaxyDbFileReader;
import io.gbloch.falcon.challenge.core.application.service.parser.FalconFileParser;
import io.gbloch.falcon.challenge.core.domain.FalconConfig;
import lombok.Getter;

/**
 * Lazy Singleton containing the Millemum Falcon configuration and the galaxy graph.
 *
 * @author Guillaume Bloch
 * Created on 09/11/2022
 */
@Getter
final class JourneyContext {
    private FalconConfig falconConfig;
    private MutableValueGraph<String, Integer> galaxy;

    private static volatile JourneyContext instance;

    private JourneyContext(
        FalconFileParser fileParser,
        GalaxyDbFileReader dbFileReader,
        String configFilePath
    ) {
        // Partly prevents reflection trickery
        if (instance != null) {
            throw new IllegalStateException("Already instantiated");
        }
        this.falconConfig = fileParser.parseFile(configFilePath);
        this.galaxy = dbFileReader.readFile(falconConfig.routesDbPath());
    }

    public static JourneyContext getInstance(
        FalconFileParser fileParser,
        GalaxyDbFileReader dbFileReader,
        String configFilePath
    ) {
        // Double check locking pattern
        if (instance == null) {
            synchronized (JourneyContext.class) {
                if (instance == null) {
                    instance = new JourneyContext(fileParser, dbFileReader, configFilePath);
                }
            }
        }
        return instance;
    }
}
