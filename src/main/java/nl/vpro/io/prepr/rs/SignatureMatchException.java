package nl.vpro.io.prepr.rs;

/**
 * @author Michiel Meeuwissen
 * @since 0.15
 */
public class SignatureMatchException extends SignatureException {
    public SignatureMatchException(String s, byte[] payload) {
        super(s, payload, false);
    }
}
