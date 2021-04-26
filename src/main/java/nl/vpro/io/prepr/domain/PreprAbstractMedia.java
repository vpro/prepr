package nl.vpro.io.prepr.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Range;

import nl.vpro.jackson2.DurationToJsonTimestamp;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Slf4j
public abstract class PreprAbstractMedia extends PreprAsset {

    @JsonSerialize(using = DurationToJsonTimestamp.Serializer.class)
    @JsonDeserialize(using = DurationToJsonTimestamp.Deserializer.class)
    Duration duration;

    //2019-0-16: Not yet documented on Prepr.
    Instant started_on;
    Instant ended_on;


    public boolean isSegment(@NonNull  Range<Instant> schedule) {
        return isSegment(schedule, Duration.ofMinutes(5));
    }


    public boolean isSegment(@NonNull  Range<Instant> schedule, @NonNull Duration slack) {
        // TODO: Segmenten worden eigenlijk niet goed ondersteund in Prepr?
        // Je kunt in de user interface allerlei filmpjes uploaden, het is in het geheel niet gezegd dat dat correspondeert met de uitzending.
        // Ze hebben echter wel een absolute 'started_on' tijd en 'duration' (of eventueel een ended_on?)
        // We gaan er nu vanuit dat een filmpje een 'segment' is, als het 'enclosed' is in het uur waarmee het geassocieerd is

        if (getStarted_on() == null) {
            log.debug("Asset {} is not a segment because it has not started_on", this);
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
        Range<Instant> extendedRange = Range.closedOpen(schedule.lowerEndpoint().minus(slack), schedule.upperEndpoint().plus(slack));

        return extendedRange.encloses(range);

    }


    @Override
    public MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
            .add("duration", duration)
            .add("name", name);
    }
}
