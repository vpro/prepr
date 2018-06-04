package nl.vpro.io.mediaconnect.domain;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
public class MCWebhookEvent {

    UUID id;

    Instant created_on;

    String event;

    MCEvent payload;
}
