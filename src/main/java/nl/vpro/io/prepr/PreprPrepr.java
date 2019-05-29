package nl.vpro.io.prepr;

import java.time.LocalDate;
import java.util.UUID;

import nl.vpro.io.prepr.domain.MCSchedule;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface PreprPrepr {

    @Deprecated
    MCSchedule getSchedule(UUID channel, LocalDate from, LocalDate until);

    @Deprecated
    default MCSchedule getSchedule(UUID channel, LocalDate from) {
        return getSchedule(channel, from, from);
    }

}
