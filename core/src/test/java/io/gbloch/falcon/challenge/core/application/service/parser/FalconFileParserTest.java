package io.gbloch.falcon.challenge.core.application.service.parser;

import static io.gbloch.falcon.challenge.core.application.TestUtils.CONFIG_FILE_PATH;
import static io.gbloch.falcon.challenge.core.application.TestUtils.CONFIG_FILE_PATH_INVALID;
import static io.gbloch.falcon.challenge.core.application.TestUtils.ENDOR;
import static io.gbloch.falcon.challenge.core.application.TestUtils.FALCON_AUTONOMY;
import static io.gbloch.falcon.challenge.core.application.TestUtils.TATOOINE;
import static io.gbloch.falcon.challenge.core.application.TestUtils.UNIVERSE_FILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import io.gbloch.falcon.challenge.core.domain.FalconConfig;
import org.junit.jupiter.api.Test;

class FalconFileParserTest {

    @Test
    void when_parseFile_then_getProperConfiguration() throws FalconFileException {
        // GIVEN
        FalconFileParser falconFileParser = new FalconFileParserImpl();

        // WHEN
        FalconConfig falconConfig = falconFileParser.parseFile(CONFIG_FILE_PATH);

        // THEN
        assertThat(falconConfig.departure()).isEqualTo(TATOOINE);
        assertThat(falconConfig.arrival()).isEqualTo(ENDOR);
        assertThat(falconConfig.autonomy()).isEqualTo(FALCON_AUTONOMY);
        assertThat(falconConfig.routesDbPath()).isEqualTo(UNIVERSE_FILE);
    }

    @Test
    void when_parseInvalidFile_then_getInvalidExcetion() {
        // GIVEN
        FalconFileParser falconFileParser = new FalconFileParserImpl();

        // WHEN
        Exception exception = catchException(
            () -> falconFileParser.parseFile(CONFIG_FILE_PATH_INVALID)
        );

        // THEN
        assertThat(exception).isExactlyInstanceOf(FalconFileInvalidException.class)
            .hasMessageContaining("Invalid");
    }

    @Test
    void when_parseNonexistingFile_then_getIOException() {
        // GIVEN
        FalconFileParser falconFileParser = new FalconFileParserImpl();

        // WHEN
        Exception exception = catchException(() -> falconFileParser.parseFile("foo.json"));

        // THEN
        assertThat(exception).isExactlyInstanceOf(FalconFileIOException.class)
            .hasMessageContaining("Error while reading");
    }
}
