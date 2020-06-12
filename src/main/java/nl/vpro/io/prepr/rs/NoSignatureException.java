package nl.vpro.io.prepr.rs;

/**
 * @author Michiel Meeuwissen
 * @since 0.15
 */
public class NoSignatureException extends SignatureException {
    public NoSignatureException(String s, byte[] payload) {
        super(s, payload);
    }
}
