package nl.vpro.io.prepr.domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.util.*;
import java.util.function.Function;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Range;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

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
@Slf4j
public class PreprEventWithDay implements Comparable<PreprEventWithDay> {
    private final LocalDate day;
    private final ZoneId zoneId;
    private final PreprEvent event;

    /**
     * When multiple {@link #event}s are actually part of one episode, then this points to the next event.
     * This is used in the calculation of {@link #asRange()}.
     *
     */
    PreprEventWithDay next = null;

    PreprEventWithDay(LocalDate day, ZoneId zoneId, PreprEvent event) {
        this.day = day;
        this.event = event;
        this.zoneId = zoneId;
    }

    public Instant getFrom() {
        return asRange().lowerEndpoint().atZone(zoneId).toInstant();
    }
    public Instant getUntil() {
        return asRange().upperEndpoint().atZone(zoneId).toInstant();
    }

    public List<PreprTimeline> getTimelines() {
        List<PreprTimeline> result = new ArrayList<>();
        PreprEventWithDay c = this;
        while(c != null) {
            result.addAll(emptyIfNull(c.getEvent().getTimelines()));
            c = c.getNext();
        }
        return result;
    }
    public PreprShow getShow() {
        return event.getShow();
    }
    public Optional<PreprTimeline> getFirstTimeline() {
        if (event == null) {
            return Optional.empty();
        }
        if (event.getTimelines() == null) {
            return Optional.empty();
        }
        return event
            .getTimelines()
            .stream()
            .findFirst()
            ;

    }

    public PreprUsers getUsers() {
        return event.getUsers();
    }

    public Range<Instant> asRange() {
        Range<Instant> range = Range.closedOpen(
            event.getFrom().atDate(day).atZone(zoneId).toInstant(),
            event.getUntil().atDate(day).atZone(zoneId).toInstant());
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
        if (event.getTimelines() != null) {
            return event.getTimelines().stream().map(AbstractPreprContent::getReference_id).findFirst().orElse(null);
        } else {
            return null;
        }
    }

    public Optional<String> episodeId() {
          if (event == null) {
              log.warn("{} has no event", this);
              return Optional.empty();
          }
        PreprEpisode episode = event.getEpisode();
        if (episode == null) {
            // this is happening a lot, so not warning, but info-ing
            log.info("{} {} has no episode! Ignoring", day, event);
            return Optional.empty();
        }
        if (episode.getId() == null) {
            log.warn("Episode {} of {} {} has no id! Ignoring", episode, day, event);
            return Optional.empty();
        }
        return Optional.of(episode.getId());
    }


    public static List<PreprEventWithDay> fromSchedule(@NonNull PreprSchedule unfilteredResult, ZoneId zoneId) {
        List<PreprEventWithDay> result = new ArrayList<>();

        Function<PreprEventWithDay, Optional<String>> matcher = PreprEventWithDay::episodeId;

        // use to be this
        //Function<PreprEventWithDay, String> matcher = PreprEventWithDay::showId;

        unfilteredResult.forEach((e) -> {
            for (PreprEvent mcEvent : e.getValue()) {
                PreprEventWithDay withDay = new PreprEventWithDay(e.getKey(), zoneId, mcEvent);
                String episodeId= matcher.apply(withDay).orElse(null);
                if (result.size() > 0) {
                    PreprEventWithDay previous = result.get(result.size() - 1);
                    String previousEpisodeId = matcher.apply(previous).orElse(null);
                    if (episodeId != null && Objects.equals(episodeId, previousEpisodeId)) {
                        log.debug("Appending {} to {}", withDay, previous);
                        previous.append(withDay);
                        continue;
                    }
                }
                result.add(withDay);
            }
        });
        Collections.sort(result);
        return result;
    }

    public static List<PreprEventWithDay> fromSchedule(@NonNull PreprSchedule unfilteredResult, @NonNull ZoneId zoneId, @NonNull LocalDateTime from, @NonNull LocalDateTime until) {
        Range<Instant> range = Range.closedOpen(from.atZone(zoneId).toInstant(), until.atZone(zoneId).toInstant());
        List<PreprEventWithDay> result = fromSchedule(unfilteredResult, zoneId);

        // episodes spanning day limits may have multliple events, keep them complete
        Set<String> episodes = new HashSet<>();
        result.forEach(event -> {
            Range<Instant> erange = event.asRange();
            boolean startInRange = range.contains(erange.lowerEndpoint());

            if (startInRange) {
                event.episodeId().ifPresent(episodes::add);
            }
        });
        result.removeIf(event -> {
            Optional<String> epId = event.episodeId();
            if (! epId.isPresent()) {
                return true;
            }
            boolean episodeInRange = episodes.contains(epId.get());
            if (!episodeInRange) {
                log.debug("The episode {} of {} is not in the range, removing", event.getEvent().getEpisode(), event);
            }
            return !episodeInRange;
        });
        return result;

    }

    @Override
    public int compareTo(PreprEventWithDay in) {
        return Comparator.nullsLast(
            Comparator.comparing(PreprEventWithDay::getDay)
                .thenComparing(PreprEventWithDay::getEvent)
        ).compare(this, in);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .omitNullValues()
            .add("day", day)
            .add("episode", episodeId())
            .add("range", asRange())
            .toString();
    }
}
