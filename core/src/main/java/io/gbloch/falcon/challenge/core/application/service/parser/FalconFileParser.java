package io.gbloch.falcon.challenge.core.application.service.parser;

import io.gbloch.falcon.challenge.core.domain.FalconConfig;

public interface FalconFileParser {

    FalconConfig parseFile(String filePath) throws FalconFileException;
}
