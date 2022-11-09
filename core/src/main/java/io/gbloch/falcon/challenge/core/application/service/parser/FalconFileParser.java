package io.gbloch.falcon.challenge.core.application.service.parser;

import io.gbloch.falcon.challenge.core.domain.FalconConfig;
import javax.validation.Validator;

public interface FalconFileParser {

    FalconConfig parseFile(String filePath) throws FalconFileException;

    // For testing purposes
    void setValidator(Validator validator);
}
