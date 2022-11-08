package io.gbloch.falcon.challenge.core.application;

import java.io.Serial;

public final class FalconCoreException extends Exception {

    @Serial
    private static final long serialVersionUID = -221573795625065014L;

    public FalconCoreException(Throwable cause) {
        super(cause);
    }
}
