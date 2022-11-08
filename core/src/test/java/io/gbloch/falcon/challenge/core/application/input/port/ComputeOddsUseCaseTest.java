package io.gbloch.falcon.challenge.core.application.input.port;

import static io.gbloch.falcon.challenge.core.application.TestUtils.CONFIG_FILE_PATH;
import static io.gbloch.falcon.challenge.core.application.TestUtils.HOTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.gbloch.falcon.challenge.core.application.FalconCoreException;
import io.gbloch.falcon.challenge.core.application.service.OddsService;
import io.gbloch.falcon.challenge.core.application.service.parser.FalconFileException;
import io.gbloch.falcon.challenge.core.application.service.parser.FalconFileParser;
import io.gbloch.falcon.challenge.core.domain.BountyHunter;
import io.gbloch.falcon.challenge.core.domain.Empire;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ComputeOddsUseCaseTest {

    @Mock
    private FalconFileParser falconFileParser;

    private static Empire createEmpire(int countdown) {
        return new Empire(countdown,
            Set.of(new BountyHunter(HOTH, 6),
                new BountyHunter(HOTH, 7),
                new BountyHunter(HOTH, 8)));
    }

    @BeforeEach
    void setUp() {
    }

    @ParameterizedTest
    @CsvSource({"6, 0", "8, 81", "9, 90", "10, 100"})
    void when_computeOddsWithCountdown_then_getExcpectedOdds(int countdown, int oddsExcepted)
        throws FalconCoreException {
        // GIVEN
        ComputeOddsUseCase oddsUseCase = new OddsService(falconFileParser);
        Empire empire = createEmpire(countdown);

        // WHEN
        int odds = oddsUseCase.whatAreTheOdds(CONFIG_FILE_PATH, empire);

        // THEN
        assertThat(oddsExcepted).isEqualTo(odds);
    }

    @Test
    void given_configParsingError_when_computeOdds_then_getException() throws FalconFileException {
        // GIVEN
        when(falconFileParser.parseFile(anyString())).thenThrow(mock(FalconFileException.class));
        Empire empire = createEmpire(0);
        ComputeOddsUseCase oddsUseCase = new OddsService(falconFileParser);

        // WHEN
        Exception exception = catchException(
            () -> oddsUseCase.whatAreTheOdds(CONFIG_FILE_PATH, empire)
        );

        // THEN
        assertThat(exception).isExactlyInstanceOf(FalconCoreException.class);
    }
}
