package io.gbloch.falcon.challenge.core.application.input.port;

import static io.gbloch.falcon.challenge.core.application.TestUtils.CONFIG_FILE_PATH;
import static io.gbloch.falcon.challenge.core.application.TestUtils.HOTH;
import static org.assertj.core.api.Assertions.assertThat;

import io.gbloch.falcon.challenge.core.application.service.OddsService;
import io.gbloch.falcon.challenge.core.domain.BountyHunter;
import io.gbloch.falcon.challenge.core.domain.Empire;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ComputeOddsUseCaseTest {

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
    void when_computeOddsWithCountdown_then_getExcpectedOdds(int countdown, int oddsExcepted) {
        // GIVEN
        ComputeOddsUseCase oddsUseCase = new OddsService();
        Empire empire = createEmpire(countdown);

        // WHEN
        int odds = oddsUseCase.whatAreTheOdds(CONFIG_FILE_PATH, empire);

        // THEN
        assertThat(oddsExcepted).isEqualTo(odds);
    }
}
