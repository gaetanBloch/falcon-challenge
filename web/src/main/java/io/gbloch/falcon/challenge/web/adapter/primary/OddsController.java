package io.gbloch.falcon.challenge.web.adapter.primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gbloch.falcon.challenge.core.application.FalconCoreException;
import io.gbloch.falcon.challenge.core.application.input.port.ComputeOddsUseCase;
import io.gbloch.falcon.challenge.core.common.ResourceFileUtils;
import io.gbloch.falcon.challenge.core.domain.Empire;
import io.smallrye.common.constraint.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Encoding;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

@Slf4j
@Path("/api/v1")
@Tag(name = "Falcon Challenge")
@RequiredArgsConstructor
public class OddsController {

    static final ObjectMapper MAPPER = new ObjectMapper();

    final ComputeOddsUseCase computeOddsUseCase;

    @Operation(
        summary = "Get the odds of reaching the destination planet"
    )
    @APIResponse(
        responseCode = "OK",
        content = @Content(schema = @Schema(implementation = Integer.class)))
    @APIResponse(
        responseCode = "INTERNAL_SERVER_EROR",
        description = "Unexpected server error",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @APIResponse(
        responseCode = "BAD_REQUEST",
        description = "Problem with the Empire file",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @Retry(delay = 1000, maxRetries = 3)
//    @Fallback(fallbackMethod = "whatAreTheOddsFallback", skipOn = FalconCoreException.class)
    @RequestBody(content = @Content(
        mediaType = MediaType.MULTIPART_FORM_DATA,
        schema = @Schema(implementation = EmpireFileInput.class),
        encoding = @Encoding(name = "file", contentType = "application/octet-stream"))
    )
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/odds")
    @POST
    public Response whatAreTheOdds(
        @Parameter(
            name = "fileInput",
            description = "Intercepted Empire JSON File",
            required = true)
        @NotNull
        @MultipartForm EmpireFileInput fileInput
    ) {
        Empire empire;
        try {
            empire = MAPPER.readValue(Files.readString(fileInput.file.toPath()), Empire.class);
        } catch (IOException e) {
            log.error("Error while reading the empire file {}", fileInput.file.toPath(), e);
            return new ErrorResponse(
                "Error while reading the empire file",
                Status.BAD_REQUEST
            ).toResponse();
        }

        // Need to get the inputstream of the resource file inside the native/UBER-JAR because
        // the file is not accessible from the file system as a regular jar file.
        File file;
        try {
            file = ResourceFileUtils.createTempFileFromResource("millenium-falcon.json");
        } catch (IOException e) {
            return new ErrorResponse(
                "Could not find the Falcon Config file",
                Status.INTERNAL_SERVER_ERROR
            ).toResponse();
        }
        try {
            int odds = computeOddsUseCase.whatAreTheOdds(file.getPath(), empire);
            return Response.ok(odds).build();
        } catch (FalconCoreException e) {
            return new ErrorResponse(e).toResponse();
        }
    }

    @Getter
    public static class EmpireFileInput {

        @Schema(type = SchemaType.STRING, format = "binary")
        @FormParam("file")
        public File file;
    }

    public record ErrorResponse(String message, Status status) {

        ErrorResponse(FalconCoreException e) {
            this(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }

        Response toResponse() {
            return Response.status(status).entity(this).build();
        }
    }
}
