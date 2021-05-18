package nl.vpro.io.prepr.rs;

import java.util.UUID;

/**
 * @author Michiel Meeuwissen
 * @since 1.0
 */
public class SignatureMatchException extends SignatureException {
    public SignatureMatchException(InvalidSignatureAction invalidSignatureAction, UUID webhookId, String s, byte[] payload) {
        super(invalidSignatureAction, webhookId, s, payload, false);
    }
}
