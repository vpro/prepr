package nl.vpro.io.mediaconnect.domain;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
public class MCAbstractObject {

    UUID id;
    Instant created_on;
    Instant changed_on;
    Instant last_seen;
    String label;
}
