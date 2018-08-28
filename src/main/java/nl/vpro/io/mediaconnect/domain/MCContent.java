package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class MCContent extends MCAbstractObject {

    Instant published_on;

    Instant unpublish_on;

    MCStatus status;

    /**
     * What does this mean?
     */
    String slug;
}
