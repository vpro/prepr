package nl.vpro.io.prepr.domain;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
public class PreprWebhookEvent {

    UUID id;

    Instant created_on;

    String event;

    String dc;

    JsonNode payload;
}
