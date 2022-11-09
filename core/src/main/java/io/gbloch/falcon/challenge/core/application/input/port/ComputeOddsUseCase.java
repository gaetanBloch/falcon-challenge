package io.gbloch.falcon.challenge.core.application.input.port;

import io.gbloch.falcon.challenge.core.application.FalconCoreException;
import io.gbloch.falcon.challenge.core.domain.Empire;
import javax.validation.Validator;

public interface ComputeOddsUseCase {
    int whatAreTheOdds(String configFilePath, Empire empire) throws FalconCoreException;

    void setValidator(Validator validator);
}
