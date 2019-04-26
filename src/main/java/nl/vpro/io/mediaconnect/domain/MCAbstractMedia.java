package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;
import java.time.Instant;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.DurationSerializer;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class MCAbstractMedia extends MCAsset {

    @JsonSerialize(using = DurationSerializer.class)
    Duration duration;

    Instant started_on;
    Instant ended_on;


    public boolean isSegment() {
        // TODO: Segmenten worden eigenlijk helemaal niet ondersteund in Prepr.
        // Je kunt in de user interface allerlei filmpjes uploaden, het is in het geheel niet gezegd dat dat correspondeert met de uitzending.
        // Misschien moeten we hier als extra check inbouwen dat we zeker weten dat dit oorspronkelijk een radiobox object was?
        // Andere optie is om hier te retourneren: false. Het wordt niet ondersteund.

        return getStarted_on() != null && getDuration() != null;

    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
            "duration='" + duration + '\'' +
            ", name='" + name + '\'' +
            ", body='" + body + '\'' +
            ", source_file=" + source_file +
            ", reference_id=" + reference_id +
            ", id=" + id +
            '}';
    }
}
