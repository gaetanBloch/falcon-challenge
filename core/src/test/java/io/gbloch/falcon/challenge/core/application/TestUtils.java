package io.gbloch.falcon.challenge.core.application;

import io.gbloch.falcon.challenge.core.domain.BountyHunter;
import io.gbloch.falcon.challenge.core.domain.Empire;
import io.gbloch.falcon.challenge.core.domain.FalconConfig;
import java.util.Set;

public final class TestUtils {

    public static final String CONFIG_FILE_PATH = "src/test/resources/millenium-falcon.json";
    public static final String CONFIG_FILE_PATH_INVALID = "src/test/resources/millenium-falcon-invalid.json";
    public static final String CONFIG_FILE_PATH_VIOLATION = "src/test/resources/millenium-falcon-violation.json";
    public static final String DB_FILE_PATH = "src/test/resources/universe.db";
    public static final String TATOOINE = "Tatooine";
    public static final String DAGOBAH = "Dagobah";
    public static final String HOTH = "Hoth";
    public static final String ENDOR = "Endor";
    public static final int FALCON_AUTONOMY = 6;
    public static final String UNIVERSE_FILE = "universe.db";
    public static final FalconConfig FALCON_CONFIG = new FalconConfig(6, TATOOINE, ENDOR, "universe.db");

    private TestUtils() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }

    public static Empire createEmpire(int countdown) {
        return new Empire(countdown,
            Set.of(new BountyHunter(HOTH, 6),
                new BountyHunter(HOTH, 7),
                new BountyHunter(HOTH, 8)));
    }
}
