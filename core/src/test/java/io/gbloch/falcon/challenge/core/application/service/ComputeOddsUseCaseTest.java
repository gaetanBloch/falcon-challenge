package io.gbloch.falcon.challenge.core.application.service;

import static io.gbloch.falcon.challenge.core.application.TestUtils.CONFIG_FILE_PATH;
import static io.gbloch.falcon.challenge.core.application.TestUtils.DAGOBAH;
import static io.gbloch.falcon.challenge.core.application.TestUtils.DB_FILE_PATH;
import static io.gbloch.falcon.challenge.core.application.TestUtils.ENDOR;
import static io.gbloch.falcon.challenge.core.application.TestUtils.FALCON_CONFIG;
import static io.gbloch.falcon.challenge.core.application.TestUtils.HOTH;
import static io.gbloch.falcon.challenge.core.application.TestUtils.TATOOINE;
import static io.gbloch.falcon.challenge.core.application.TestUtils.createEmpire;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.RowSortedTable;
import com.google.common.collect.TreeBasedTable;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import io.gbloch.falcon.challenge.core.application.FalconCoreException;
import io.gbloch.falcon.challenge.core.application.input.port.ComputeOddsUseCase;
import io.gbloch.falcon.challenge.core.application.service.db.GalaxyDbException;
import io.gbloch.falcon.challenge.core.application.service.db.GalaxyDbFileReader;
import io.gbloch.falcon.challenge.core.application.service.parser.FalconFileException;
import io.gbloch.falcon.challenge.core.application.service.parser.FalconFileParser;
import io.gbloch.falcon.challenge.core.domain.BountyHunter;
import io.gbloch.falcon.challenge.core.domain.Empire;
import io.gbloch.falcon.challenge.core.domain.FalconConfig;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
class ComputeOddsUseCaseTest {

    @Mock
    private FalconFileParser falconFileParser;
    @Mock
    private GalaxyDbFileReader dbFileReader;

    private final MutableValueGraph<String, Integer> galaxy = ValueGraphBuilder.directed().build();

    @BeforeEach
    void setUp() {
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
        Mockito.lenient().when(falconFileParser.parseFile(CONFIG_FILE_PATH)).thenReturn(FALCON_CONFIG);
        ComputeOddsUseCase oddsUseCase = new OddsService(falconFileParser, dbFileReader);
        Empire empire = createEmpire(countdown);

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

        // WHEN
        Exception exception = catchException(
            () -> oddsUseCase.whatAreTheOdds(CONFIG_FILE_PATH, empire)
        );

        // THEN
        assertThat(exception).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
