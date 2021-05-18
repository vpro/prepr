package nl.vpro.io.prepr.rs;

/**
 * @author Michiel Meeuwissen
 * @since 1.0
 */
public class NoSignatureException extends SignatureException {
    public NoSignatureException(InvalidSignatureAction invalidSignatureAction, String s, byte[] payload) {
        super(invalidSignatureAction, null, s, payload, false);
    }
}
