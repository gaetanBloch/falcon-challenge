package io.gbloch.falcon.challenge.core.application.service.parser;

import java.io.Serial;

final class FalconFileInvalidException extends FalconFileException {

    @Serial
    private static final long serialVersionUID = 5945099776917031057L;

    public FalconFileInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public FalconFileInvalidException(String message) {
        super(message);
    }

}
