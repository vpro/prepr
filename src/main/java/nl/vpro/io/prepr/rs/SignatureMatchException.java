package nl.vpro.io.prepr.rs;

import java.io.Serial;
import java.util.UUID;

/**
 * @author Michiel Meeuwissen
 * @since 1.0
 */
public class SignatureMatchException extends SignatureException {
    @Serial
    private static final long serialVersionUID = 6656225211075617637L;

    public SignatureMatchException(InvalidSignatureAction invalidSignatureAction, String channel, UUID webhookId, String s, byte[] payload) {
        super(invalidSignatureAction, channel, webhookId, s, payload, false);
    }
}
