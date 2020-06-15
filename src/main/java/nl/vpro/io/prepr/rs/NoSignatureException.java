package nl.vpro.io.prepr.rs;

/**
 * @author Michiel Meeuwissen
 * @since 1.0
 */
public class NoSignatureException extends SignatureException {
    public NoSignatureException(String s, byte[] payload) {
        super(null, s, payload, false);
    }
}
