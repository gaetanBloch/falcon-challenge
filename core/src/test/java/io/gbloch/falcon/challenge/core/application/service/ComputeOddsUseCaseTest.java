package io.gbloch.falcon.challenge.core.application.service;

import static io.gbloch.falcon.challenge.core.application.TestUtils.CONFIG_FILE_PATH;
import static io.gbloch.falcon.challenge.core.application.TestUtils.DAGOBAH;
import static io.gbloch.falcon.challenge.core.application.TestUtils.ENDOR;
import static io.gbloch.falcon.challenge.core.application.TestUtils.FALCON_CONFIG;
import static io.gbloch.falcon.challenge.core.application.TestUtils.HOTH;
import static io.gbloch.falcon.challenge.core.application.TestUtils.TATOOINE;
import static io.gbloch.falcon.challenge.core.application.TestUtils.createEmpire;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.mock;

import com.google.common.collect.RowSortedTable;
import com.google.common.collect.TreeBasedTable;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import io.gbloch.falcon.challenge.core.application.FalconCoreException;
import io.gbloch.falcon.challenge.core.application.input.port.ComputeOddsUseCase;
import io.gbloch.falcon.challenge.core.application.service.db.GalaxyDbFileReader;
import io.gbloch.falcon.challenge.core.application.service.db.GalaxyDbFileReaderImpl;
import io.gbloch.falcon.challenge.core.application.service.parser.FalconFileParser;
import io.gbloch.falcon.challenge.core.application.service.parser.FalconFileParserImpl;
import io.gbloch.falcon.challenge.core.domain.Empire;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

@QuarkusTest
class ComputeOddsUseCaseTest {

    private final MutableValueGraph<String, Integer> galaxy = ValueGraphBuilder.directed().build();
    @Inject
    Validator validator;
    @Inject
    FalconFileParser falconFileParser;
    @Inject
    GalaxyDbFileReader dbFileReader;

    @BeforeEach
    void setUp() {
        QuarkusMock.installMockForType(mock(FalconFileParserImpl.class), FalconFileParser.class);
        QuarkusMock.installMockForType(mock(GalaxyDbFileReaderImpl.class),
            GalaxyDbFileReader.class);

        final RowSortedTable<String, String, Integer> routes = TreeBasedTable.create();
        routes.put(TATOOINE, DAGOBAH, 6);
        routes.put(DAGOBAH, ENDOR, 4);
        routes.put(DAGOBAH, HOTH, 1);
        routes.put(HOTH, ENDOR, 1);
        routes.put(TATOOINE, HOTH, 6);
        routes.rowKeySet().forEach((k) ->
            routes.row(k).forEach((k2, v) -> this.galaxy.putEdgeValue(k, k2, v)));
    }

    @ParameterizedTest
    @CsvSource({"6, 0", "8, 81", "9, 90", "10, 100"})
    void when_computeOddsWithCountdown_then_getExcpectedOdds(int countdown, int oddsExcepted)
        throws FalconCoreException {
        // GIVEN
        Mockito.lenient().when(dbFileReader.readFile("universe.db")).thenReturn(galaxy);
        Mockito.lenient().when(falconFileParser.parseFile(CONFIG_FILE_PATH))
            .thenReturn(FALCON_CONFIG);
        ComputeOddsUseCase oddsUseCase = new OddsService(falconFileParser, dbFileReader);
        Empire empire = createEmpire(countdown);
        oddsUseCase.setValidator(validator);

        // WHEN
        int odds = oddsUseCase.whatAreTheOdds(CONFIG_FILE_PATH, empire);

        // THEN
        assertThat(odds).isEqualTo(oddsExcepted);
    }

    @Test
    void given_invalidEmpire_when_computeOdds_then_getException() {
        // GIVEN
        Empire empire = createEmpire(-1);
        ComputeOddsUseCase oddsUseCase = new OddsService(falconFileParser, dbFileReader);
        oddsUseCase.setValidator(validator);

        // WHEN
        Exception exception = catchException(
            () -> oddsUseCase.whatAreTheOdds(CONFIG_FILE_PATH, empire)
        );

        // THEN
        assertThat(exception).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
