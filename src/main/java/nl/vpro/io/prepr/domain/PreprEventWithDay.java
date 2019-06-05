package nl.vpro.io.prepr.domain;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.google.common.collect.Range;

import nl.vpro.util.BindingUtils;

/**
 * The guides call returns data in format which is often unsuitable for processing. It will group by day, but broadcasts may span days.
 * Also, the contained event objects are not self contained, and need the day to calculate the actual time.
 *
 * I don't know yet how it works with time zones.
 *
 * This class wraps the {@link PreprEvent} with {@link LocalDate} object to get a complete small bundle of information which can be used as an entry in lists which represent a schedule.
 *
 * The utility {@link #asRange()} returns the actual range the event is representing.
 *
 * The {@link #append(PreprEventWithDay)} utility targets to be able to 'glue' events together.
 *
 * @author Michiel Meeuwissen
 * @since 0.9
 */
@Getter
@ToString
@Slf4j
public class PreprEventWithDay {
    private final LocalDate day;
    private final PreprEvent mcEvent;

    PreprEventWithDay next = null;


    PreprEventWithDay(LocalDate day, PreprEvent mcEvent) {
        this.day = day;
        this.mcEvent = mcEvent;
    }

    public Instant getFrom() {
        return asRange().lowerEndpoint().atZone(BindingUtils.DEFAULT_ZONE).toInstant();
    }
    public Instant getUntil() {
        return asRange().upperEndpoint().atZone(BindingUtils.DEFAULT_ZONE).toInstant();
    }

    public Range<LocalDateTime> asRange() {

        Range<LocalDateTime> range = mcEvent.getRange(day);
        if (next != null) {
            return range.span(next.asRange());
        } else {
            return range;
        }
    }

    public void append(PreprEventWithDay next) {
        if (this.next == null) {
            this.next = next;
        } else {
            this.next.append(next);
        }
    }

    public String showId() {
        if (mcEvent.getTimelines() != null) {
            return mcEvent.getTimelines().stream().map(PreprContent::getReference_id).findFirst().orElse(null);
        } else {
            return null;
        }
    }


    public static List<PreprEventWithDay> fromSchedule(@Nonnull PreprSchedule unfilteredResult) {
        List<PreprEventWithDay> result = new ArrayList<>();

        unfilteredResult.forEach((e) -> {
            for (PreprEvent mcEvent : e.getValue()) {
                PreprEventWithDay withDay = new PreprEventWithDay(e.getKey(), mcEvent);
                String showId = withDay.showId();
                if (result.size() > 0) {
                    PreprEventWithDay previous = result.get(result.size() - 1);
                    String previousShowId = previous.showId();
                    if (showId != null && Objects.equals(showId, previousShowId)) {
                        log.debug("Appending {} to {}", withDay, previous);
                        previous.append(withDay);
                        continue;
                    }
                }
                result.add(withDay);
            }
        });
        return result;
    }

    public static List<PreprEventWithDay> fromSchedule(@Nonnull PreprSchedule unfilteredResult, @Nonnull LocalDateTime from, @Nonnull LocalDateTime until) {
        Range<LocalDateTime> range = Range.closedOpen(from, until);
        List<PreprEventWithDay> result = fromSchedule(unfilteredResult);

        result.removeIf(event -> {
            Range<LocalDateTime> erange = event.asRange();
            boolean startInRange = range.contains(erange.lowerEndpoint());
            if (!startInRange) {
                log.debug("{} not in {}: Removing {})", erange.lowerEndpoint(), range, event);
            }
            return !startInRange;
        });
        return result;

    }

}
