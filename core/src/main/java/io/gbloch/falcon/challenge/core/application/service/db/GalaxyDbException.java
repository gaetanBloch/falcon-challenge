package io.gbloch.falcon.challenge.core.application.service.db;

import java.io.Serial;

public final class GalaxyDbException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1913532098995801290L;

    GalaxyDbException(String message, Throwable cause) {
        super(message, cause);
    }
}
