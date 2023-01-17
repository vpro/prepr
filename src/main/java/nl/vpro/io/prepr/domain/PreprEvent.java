package nl.vpro.io.prepr.domain;

import lombok.Data;

import java.time.*;
import java.util.*;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.collect.Range;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@JsonDeserialize(converter= PreprEvent.Deserializer.class)
public class PreprEvent implements Comparable<PreprEvent> {

    String id; // no idea

    LocalTime from;

    LocalTime until;

    // pleonastic
    Integer offset;

    Integer limit;

    PreprShowSchedule rules;

    PreprGuide guide;

    PreprShow show;

    // No idea what this is.
    String clock;

    PreprUsers users;

    List<PreprTimeline> timelines;

    PreprEpisode episode;


    public Range<LocalDateTime> getRange(LocalDate day) {
        return Range.closedOpen(day.atTime(from), day.atTime(until));
    }

    @Override
    public int compareTo(PreprEvent o) {
        return Comparator.comparing(PreprEvent::getFrom)
            .compare(this, o);

    }

    /**
     * We want the timelines to be in a logical order. That is, the first one first.
     * We get them in a kind of random order back from the API.
     * <p>
     * We use the _first_ timeline to generate a ID, so it should always be the same one.
     */

    public static class Deserializer extends StdConverter<PreprEvent, PreprEvent> {

        @Override
        public PreprEvent convert(PreprEvent mcEvent) {
            if (mcEvent != null && mcEvent.timelines != null) {
                mcEvent.timelines.sort((t1, t2) -> Objects.compare(t1.getFrom(), t2.getFrom(), Comparator.naturalOrder()));
            }
            return mcEvent;
        }
    }

}
