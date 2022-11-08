package io.gbloch.falcon.challenge.core.application.service.parser;

import java.io.Serial;

public abstract class FalconFileException extends Exception {

    @Serial
    private static final long serialVersionUID = 5797945997916290445L;

    FalconFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
