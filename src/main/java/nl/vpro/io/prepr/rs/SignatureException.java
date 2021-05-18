package nl.vpro.io.prepr.rs;

import lombok.Getter;

import java.util.UUID;

/**
 * @author Michiel Meeuwissen
 * @since 1.0
 */
@Getter
public abstract class SignatureException extends SecurityException {

    private final byte[] payload;

    private final boolean temporary;

    private final UUID webhookId;

    private final InvalidSignatureAction invalidSignatureAction;

    protected SignatureException(InvalidSignatureAction invalidSignatureAction, UUID webhookId, String s, byte[] payload, boolean temporary) {
        super(s);
        this.invalidSignatureAction = invalidSignatureAction;
        this.webhookId = webhookId;
        this.payload = payload;
        this.temporary = temporary;
    }
}
