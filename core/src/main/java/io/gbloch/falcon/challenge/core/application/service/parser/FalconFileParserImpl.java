package io.gbloch.falcon.challenge.core.application.service.parser;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gbloch.falcon.challenge.core.domain.FalconConfig;
import java.io.File;
import java.io.IOException;
import javax.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import javax.validation.Validation;
import javax.validation.Validator;

@Slf4j
@ApplicationScoped
final class FalconFileParserImpl implements FalconFileParser {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public FalconConfig parseFile(String filePath) {
        FalconConfig falconConfig;
        try {
            falconConfig = MAPPER.readValue(new File(filePath), FalconConfig.class);
            falconConfig.validate();
        } catch (JsonMappingException e) {
            logDbError(e);
            throw new FalconFileInvalidException(
                    "Invalid Millenium Falcon Configuration File: " + filePath, e);
        } catch (IOException e) {
            logDbError(e);
            throw new FalconFileIOException(
                    "Error while reading Millenium Falcon Configuration File: " + filePath, e);
        }

        return falconConfig;
    }

    private void logDbError(Exception e) {
        log.error("Error with db file", e);
    }
}
