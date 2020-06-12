package nl.vpro.io.prepr.rs;

import lombok.Getter;

/**
 * @author Michiel Meeuwissen
 * @since 0.15
 */
@Getter
public abstract class SignatureException extends SecurityException {
    byte[] payload;

    public SignatureException(String s, byte[] payload) {
        super(s);
        this.payload = payload;
    }
}
