package io.gbloch.falcon.challenge.core.application.service.parser;

import java.io.Serial;

final class FalconFileIOException extends FalconFileException {
    @Serial
    private static final long serialVersionUID = 4221439064761356253L;

    public FalconFileIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
