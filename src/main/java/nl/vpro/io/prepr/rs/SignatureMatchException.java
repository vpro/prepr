package nl.vpro.io.prepr.rs;

import lombok.Getter;

/**
 * @author Michiel Meeuwissen
 * @since 0.15
 */
@Getter
public class SignatureMatchException extends SecurityException {
    byte[] payload;

    public SignatureMatchException(String s, byte[] payload) {
        super(s);
        this.payload = payload;
    }
}
