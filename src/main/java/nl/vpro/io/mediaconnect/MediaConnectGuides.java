package nl.vpro.io.mediaconnect;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.annotation.Nonnull;

import com.google.common.collect.Range;

import nl.vpro.io.mediaconnect.domain.MCGuide;
import nl.vpro.io.mediaconnect.domain.MCItems;
import nl.vpro.io.mediaconnect.domain.MCSchedule;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface MediaConnectGuides {




    MCSchedule getSchedule(LocalDate from, LocalDate until, boolean exceptions);

    default MCSchedule getSchedule(@Nonnull  LocalDateTime from, @Nonnull  LocalDateTime until) {
        Range<LocalDateTime> range = Range.openClosed(from, until);
        MCSchedule result = getSchedule(from.toLocalDate(), until.toLocalDate(), true);
        result.forEach((e) -> {
            e.getValue().removeIf(event -> !range.encloses(event.getRange(e.getKey())));

        });
        return result;
    }

    default MCSchedule getSchedule(LocalDate from, LocalDate until) {
        return getSchedule(from, until, true);
    }

    default MCSchedule getSchedule(LocalDate from) {
        return getSchedule(from, from);
    }

     MCItems<MCGuide> getGuides(String q);



}
