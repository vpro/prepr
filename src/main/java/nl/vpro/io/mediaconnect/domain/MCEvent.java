package nl.vpro.io.mediaconnect.domain;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.collect.Range;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@JsonDeserialize(converter= MCEvent.Deserializer.class)
public class MCEvent {

    //String id; // no idea

    LocalTime from;

    LocalTime until;

    // pleonastic
    Integer offset;

    Integer limit;

    MCRule rules;

    MCGuide guide;

    MCShow show;

    // No idea what this is.
    String clock;

    MCUsers users;

    List<MCTimeline> timelines;

    MCEpisode episode;


    public Range<LocalDateTime> getRange(LocalDate day) {
        return Range.closedOpen(day.atTime(from), day.atTime(until));
    }


    /**
     * We want the timelines to be in a logical order. That is, the first one first.
     * We get them in a kind of random order back from the API.
     *
     * We use the _first_ timeline to generate a ID, so it should always be the same one.
     */

    public static class Deserializer extends StdConverter<MCEvent, MCEvent> {

        @Override
        public MCEvent convert(MCEvent mcEvent) {
            if (mcEvent != null && mcEvent.timelines != null) {
                mcEvent.timelines.sort((t1, t2) -> Objects.compare(t1.getFrom(), t2.getFrom(), Comparator.naturalOrder()));
            }
            return mcEvent;
        }
    }

}
