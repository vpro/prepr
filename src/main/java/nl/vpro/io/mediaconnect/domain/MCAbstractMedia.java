package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;
import java.time.Instant;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.DurationSerializer;
import com.google.common.collect.Range;

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


    public boolean isSegment(@Nonnull Range<Instant> schedule) {
        // TODO: Segmenten worden eigenlijk niet goed ondersteund in Prepr?
        // Je kunt in de user interface allerlei filmpjes uploaden, het is in het geheel niet gezegd dat dat correspondeert met de uitzending.
        // Ze hebben echter wel een absolute 'started_on' tijd en 'duration' (of eventueel een ended_on?)
        // We gaan er nu vanuit dat een filmpje een 'segment' is, als het 'enclosed' is in het uur waarmee het geassocieerd is

        if (getStarted_on() == null) {
            return false;
        }
        Range<Instant> range;
        if (getDuration() != null) {
            range = Range.closedOpen(getStarted_on(), getStarted_on().plus(getDuration()));
        } else if (getEnded_on() != null) {
            range = Range.closedOpen(getStarted_on(), getEnded_on());
        } else {
            return false;
        }

        return schedule.encloses(range);

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
