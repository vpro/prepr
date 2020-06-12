package nl.vpro.io.prepr.rs;

import lombok.Getter;

/**
 * @author Michiel Meeuwissen
 * @since 0.15
 */
@Getter
public abstract class SignatureException extends SecurityException {
    private final byte[] payload;

    private final boolean temporary;

    protected SignatureException(String s, byte[] payload, boolean temporary) {
        super(s);
        this.payload = payload;
        this.temporary = temporary;
    }
}
