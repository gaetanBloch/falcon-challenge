package io.gbloch.falcon.challenge.core.application.service.db;

import static io.gbloch.falcon.challenge.core.application.TestUtils.DAGOBAH;
import static io.gbloch.falcon.challenge.core.application.TestUtils.DB_FILE_PATH;
import static io.gbloch.falcon.challenge.core.application.TestUtils.ENDOR;
import static io.gbloch.falcon.challenge.core.application.TestUtils.HOTH;
import static io.gbloch.falcon.challenge.core.application.TestUtils.TATOOINE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.google.common.graph.MutableValueGraph;
import org.junit.jupiter.api.Test;

class GalaxyDbFileReaderTest {
    @Test
    void when_readDbFile_then_getTableRows() throws GalaxyDbException {
        // GIVEN
        GalaxyDbFileReader galaxyDbFileReader = new GalaxyDbFileReaderImpl();

        // WHEN
        MutableValueGraph galaxy = galaxyDbFileReader.readFile(DB_FILE_PATH);

        // THEN
        assertThat(galaxy.edges().size()).isEqualTo(5);
        assertThat(galaxy.nodes().size()).isEqualTo(4);
        assertThat(galaxy.edgeValueOrDefault(TATOOINE, DAGOBAH, 0)).isEqualTo(6);
        assertThat(galaxy.edgeValueOrDefault(TATOOINE, HOTH, 0)).isEqualTo(6);
        assertThat(galaxy.edgeValueOrDefault(DAGOBAH, HOTH, 0)).isEqualTo(1);
        assertThat(galaxy.edgeValueOrDefault(DAGOBAH, ENDOR, 0)).isEqualTo(4);
        assertThat(galaxy.edgeValueOrDefault(HOTH, ENDOR, 0)).isEqualTo(1);
    }

    @Test
    void when_readDbFile_then_getGalaxyExcetion() {
        // GIVEN
        GalaxyDbFileReader galaxyDbFileReader = new GalaxyDbFileReaderImpl();

        // WHEN
        Exception exception = catchException(() ->
            galaxyDbFileReader.readFile("src/test/resources/foo.db")
        );

        // THEN
        assertThat(exception).isExactlyInstanceOf(GalaxyDbException.class)
            .hasMessageContaining("Error while reading");
    }
}
