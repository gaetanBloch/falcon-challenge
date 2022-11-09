package io.gbloch.falcon.challenge.adapter.primary;

import static org.assertj.core.api.Assertions.assertThat;

import io.gbloch.falcon.challenge.core.application.input.port.ComputeOddsUseCase;
import io.quarkus.test.junit.main.Launch;
import io.quarkus.test.junit.main.LaunchResult;
import io.quarkus.test.junit.main.QuarkusMainTest;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusMainTest
class CliTest {

    @Inject
    ComputeOddsUseCase computeOddsUseCase;

    @Test
    @Launch({"src/main/resources/millenium-falcon.json", "src/main/resources/empire.json"})
    void when_executeCommand_then_getOdds(LaunchResult result) {
        assertThat(result.exitCode()).isEqualTo(0);
        assertThat(result.getOutput()).contains("100");
    }

    @Test
    @Launch({"src/main/resources/millenium-falcon.json", "bar"})
    void when_executeCommandWithAbsentEmpireFile_then_getException(LaunchResult result) {
        assertThat(result.getErrorOutput()).contains("Error while reading the empire file");
    }

    @Test
    @Launch({"foo", "src/main/resources/empire.json"})
    void when_executeCommandWithAbsentFalconConfigFile_then_getException(LaunchResult result) {
        assertThat(result.getErrorOutput()).contains("Error while calculating the odds");
    }
}
