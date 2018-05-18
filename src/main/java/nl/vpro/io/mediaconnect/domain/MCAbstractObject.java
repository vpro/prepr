package nl.vpro.io.mediaconnect.domain;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "label")
@JsonSubTypes({
    @JsonSubTypes.Type(value = MCWebhook.class, name = "Webhook"),
    @JsonSubTypes.Type(value = MCTimeline.class, name = "Timeline"),
    @JsonSubTypes.Type(value = MCShow.class, name = "Show")
})

public class MCAbstractObject  {

    UUID id;
    Instant created_on;
    Instant changed_on;
    Instant last_seen;
    //String label;
}
