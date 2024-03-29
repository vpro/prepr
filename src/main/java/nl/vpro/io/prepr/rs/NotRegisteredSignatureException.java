package nl.vpro.io.prepr.rs;

import java.io.Serial;

/**
 * @author Michiel Meeuwissen
 * @since 1.0
 */
public class NotRegisteredSignatureException extends SignatureException {
    @Serial
    private static final long serialVersionUID = 8646632411275924979L;

    public NotRegisteredSignatureException(InvalidSignatureAction invalidSignatureAction, String channel, String s, byte[] payload) {
        super(invalidSignatureAction, channel, null, s, payload, true);    }
}
