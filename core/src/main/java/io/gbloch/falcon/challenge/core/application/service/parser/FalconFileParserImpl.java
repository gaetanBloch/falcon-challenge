package io.gbloch.falcon.challenge.core.application.service.parser;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gbloch.falcon.challenge.core.domain.FalconConfig;
import java.io.File;
import java.io.IOException;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
final class FalconFileParserImpl implements FalconFileParser {

    @Override
    public FalconConfig parseFile(String filePath) {
        FalconConfig falconConfig;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            falconConfig = objectMapper.readValue(new File(filePath), FalconConfig.class);
        } catch (JsonMappingException e) {
            throw new FalconFileInvalidException(
                    "Invalid Millenium Falcon Configuration File: " + filePath, e);
        } catch (IOException e) {
            throw new FalconFileIOException(
                    "Error while reading Millenium Falcon Configuration File: " + filePath, e);
        }

        return falconConfig;
    }
}
