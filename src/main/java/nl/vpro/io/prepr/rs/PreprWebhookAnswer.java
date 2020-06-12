package nl.vpro.io.prepr.rs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
@Data
@AllArgsConstructor
@lombok.Builder
public class PreprWebhookAnswer {
    String message;
    UUID webhookid;
    String channel;


}
