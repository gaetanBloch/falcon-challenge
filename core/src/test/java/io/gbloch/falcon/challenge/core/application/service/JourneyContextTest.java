package io.gbloch.falcon.challenge.core.application.service;


import static io.gbloch.falcon.challenge.core.application.TestUtils.CONFIG_FILE_PATH;
import static io.gbloch.falcon.challenge.core.application.TestUtils.FALCON_CONFIG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.gbloch.falcon.challenge.core.application.service.db.GalaxyDbException;
import io.gbloch.falcon.challenge.core.application.service.db.GalaxyDbFileReader;
import io.gbloch.falcon.challenge.core.application.service.parser.FalconFileException;
import io.gbloch.falcon.challenge.core.application.service.parser.FalconFileParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JourneyContextTest {

    @Mock
    private FalconFileParser falconFileParser;
    @Mock
    private GalaxyDbFileReader dbFileReader;

    @Test
    void given_configParsingError_when_computeOdds_then_getException() {
        // GIVEN
        when(falconFileParser.parseFile(anyString())).thenThrow(mock(FalconFileException.class));

        // WHEN
        Exception exception = catchException(
            () -> JourneyContext.getInstance(falconFileParser, dbFileReader, CONFIG_FILE_PATH)
        );

        // THEN
        assertThat(exception).isInstanceOf(FalconFileException.class);
    }

    @Test
    void given_galaxyDbError_when_computeOdds_then_getException() {
        // GIVEN
        when(dbFileReader.readFile(anyString())).thenThrow(GalaxyDbException.class);
        when(falconFileParser.parseFile(anyString())).thenReturn(FALCON_CONFIG);

        // WHEN
        Exception exception = catchException(
            () -> JourneyContext.getInstance(falconFileParser, dbFileReader, CONFIG_FILE_PATH)
        );

        // THEN
        assertThat(exception).isExactlyInstanceOf(GalaxyDbException.class);
    }
}
