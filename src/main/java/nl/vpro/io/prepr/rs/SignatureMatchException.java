package nl.vpro.io.prepr.rs;

import java.util.UUID;

/**
 * @author Michiel Meeuwissen
 * @since 0.15
 */
public class SignatureMatchException extends SignatureException {
    public SignatureMatchException(UUID webhookId, String s, byte[] payload) {
        super(webhookId, s, payload, false);
    }
}
