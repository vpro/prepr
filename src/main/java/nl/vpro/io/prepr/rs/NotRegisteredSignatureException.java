package nl.vpro.io.prepr.rs;

/**
 * @author Michiel Meeuwissen
 * @since 1.0
 */
public class NotRegisteredSignatureException extends SignatureException {
    public NotRegisteredSignatureException(InvalidSignatureAction invalidSignatureAction, String channel, String s, byte[] payload) {
        super(invalidSignatureAction, channel, null, s, payload, true);    }
}
