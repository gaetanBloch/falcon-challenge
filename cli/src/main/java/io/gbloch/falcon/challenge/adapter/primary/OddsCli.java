package io.gbloch.falcon.challenge.adapter.primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gbloch.falcon.challenge.core.application.FalconCoreException;
import io.gbloch.falcon.challenge.core.application.input.port.ComputeOddsUseCase;
import io.gbloch.falcon.challenge.core.domain.Empire;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import java.io.File;
import java.io.IOException;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.IFactory;
import picocli.CommandLine.Parameters;

@Slf4j
@Command(
    name = "give-me-the-odds",
    mixinStandardHelpOptions = true,
    version = "give-me-the-odds 1.0.0",
    description = "Get the odds of reaching the destination planet"
)
@QuarkusMain
public class OddsCli implements Runnable, QuarkusApplication {

    static final ObjectMapper MAPPER = new ObjectMapper();
    @Inject
    IFactory factory;
    @Inject
    ComputeOddsUseCase computeOddsUseCase;
    @Parameters(index = "0", description = "The abslute or relative path to the Millenium Falcon's config file")
    private String falconConfigFilePath;
    @Parameters(index = "1", description = "The absolute or relative path to the Intercepted Empire file")
    private String empireFilePath;

    @Override
    public int run(String... args) {
        return new CommandLine(this, factory).execute(args);
    }

    @Override
    public void run() {
        Empire empire;
        try {
            empire = MAPPER.readValue(new File(empireFilePath), Empire.class);
        } catch (IOException e) {
            System.err.println(
                "Error while reading the empire file: " + empireFilePath + ". " + e);
            return;
        }
        try {
            System.out.println(
                computeOddsUseCase.whatAreTheOdds(new File(falconConfigFilePath).getAbsolutePath(),
                    empire));
        } catch (FalconCoreException e) {
            System.err.println("Error while calculating the odds: " + e);
        }
    }
}
