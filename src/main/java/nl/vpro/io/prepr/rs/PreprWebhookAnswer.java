package nl.vpro.io.prepr.rs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

/**
 * When implementing a webhook as defined by prepr APIs we use this as a return object for (successful) responses (i.e. its
 * json binding)
 *
 * It is afaik not defined
 * @author Michiel Meeuwissen
 * @since 1.0
 */
@Data
@AllArgsConstructor
@lombok.Builder
public class PreprWebhookAnswer {
    String message;
    UUID webhookid;
    String channel;
}
