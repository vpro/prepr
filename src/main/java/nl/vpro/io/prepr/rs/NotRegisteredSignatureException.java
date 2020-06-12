package nl.vpro.io.prepr.rs;

/**
 * @author Michiel Meeuwissen
 * @since 0.15
 */
public class NotRegisteredSignatureException extends SignatureException {
    public NotRegisteredSignatureException(String s, byte[] payload) {
        super(s, payload, true);    }
}
