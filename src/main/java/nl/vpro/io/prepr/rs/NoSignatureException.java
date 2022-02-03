package nl.vpro.io.prepr.rs;

/**
 * @author Michiel Meeuwissen
 * @since 1.0
 */
public class NoSignatureException extends SignatureException {
    private static final long serialVersionUID = -5900579990039004086L;

    public NoSignatureException(InvalidSignatureAction invalidSignatureAction, String channel, String s, byte[] payload) {
        super(invalidSignatureAction, channel, null, s, payload, false);
    }
}
