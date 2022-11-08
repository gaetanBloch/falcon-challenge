package io.gbloch.falcon.challenge.core.application;

public final class TestUtils {

    public static final String CONFIG_FILE_PATH = "src/test/resources/millenium-falcon.json";
    public static final String CONFIG_FILE_PATH_INVALID = "src/test/resources/millenium-falcon-invalid.json";
    public static final String TATOOINE = "Tatooine";
    public static final String DAGOBAH = "Dagobah";
    public static final String HOTH = "Hoth";
    public static final String ENDOR = "Endor";
    public static final int FALCON_AUTONOMY = 6;
    public static final String UNIVERSE_FILE = "universe.db";

    private TestUtils() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }
}
