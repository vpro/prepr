package nl.vpro.io.prepr.rs;

/**
 * @author Michiel Meeuwissen
 * @since 1.0
 */
public class NotRegisteredSignatureException extends SignatureException {
    public NotRegisteredSignatureException(String s, byte[] payload) {
        super(null, s, payload, true);    }
}
