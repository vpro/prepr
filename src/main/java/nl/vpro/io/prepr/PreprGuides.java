package nl.vpro.io.prepr;

import java.time.*;
import java.util.List;
import java.util.UUID;

import org.checkerframework.checker.nullness.qual.NonNull;;

import nl.vpro.io.prepr.domain.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface PreprGuides {


    ZoneId getZone();

    PreprSchedule getSchedule(@NonNull LocalDate from, @NonNull LocalDate until, boolean exceptions, UUID showId);

    /**
     * Performs {@link #getSchedule(LocalDateTime, LocalDateTime)}, and wraps the result into a list of {@link PreprEventWithDay}
     */

    default List<PreprEventWithDay> getSchedule(@NonNull  LocalDateTime from, @NonNull  LocalDateTime until) {

        // broadcasts may span 0 o'clock, so we need the day before and after too, to make sure that we get only _complete_ broadcasts
        LocalDate fromDate = from.toLocalDate().minusDays(1);
        LocalDate untilDate = until.toLocalDate().plusDays(1);
        if (Duration.between(untilDate.atStartOfDay(), until).toMinutes() == 0) {
            // Well if the until day is exactly at the start of day, so actually belonging to the next day, we don't need any data for it.
            untilDate = untilDate.minusDays(1);
        }
        PreprSchedule unfilteredResult = getSchedule(fromDate, untilDate, true, null);
        return PreprEventWithDay.fromSchedule(unfilteredResult, getZone(), from, until);
    }

    default PreprSchedule getSchedule(@NonNull LocalDate from, @NonNull LocalDate until) {
        return getSchedule(from, until, true, null);
    }

    default PreprSchedule getSchedule(@NonNull LocalDate from) {
        return getSchedule(from, from);
    }

     PreprItems<PreprGuide> getGuides(@NonNull String q);


}
